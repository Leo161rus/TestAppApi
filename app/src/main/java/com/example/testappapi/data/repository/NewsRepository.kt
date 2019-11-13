package com.example.testappapi.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.testappapi.common.ListUtil
import com.example.testappapi.common.networkConnection
import com.example.testappapi.data.FailureException
import com.example.testappapi.data.NetworkException
import com.example.testappapi.data.local.DataSource
import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.model.statusType.StatusType
import com.example.testappapi.data.remote.NewsApiRemote
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val context : Context,
    private val newsApiRemote: NewsApiRemote,
    private val dataSource: DataSource
) {

    companion object {
        private const val TAG = "NewsRepository"
        private const val LIST_IS_EMPTY = 0
        private const val FIRST_PAGE = 1
    }

    fun getNews(pageNum: Int, isRefresh: Boolean): Single<ArrayList<Article>> =
        when (isRefresh) {
            true -> operationFetchAndPersNews(FIRST_PAGE)
            else -> checkOperatationFetchAndInsert(pageNum)
        }

    private fun mergeNews(
        currentNews: ArrayList<Article>,
        nextPage: Int
    ): Single<ArrayList<Article>> =
        when (networkConnection(context)) {
            true -> {
                newsApiRemote.getNews(nextPage)
                    .flatMap {
                        when (it.status) {
                            StatusType.STATUS_OK.toLowerCase() -> {
                                Single.just(it)
                            }
                            else -> Single.error(FailureException())
                        }
                    }
                    .doAfterSuccess {
                        dataSource.saveNewsResult(it.totalResults)
                    }
                    .map { it.articles }
                    .map { nextNews ->
                        currentNews.addAll(nextNews)
                        currentNews
                    }
                    .doOnSuccess {
                        Log.i(TAG, "database remove news")
                        delete()
                    }
                    .doAfterSuccess {
                        Log.i(TAG, "merg from api size by ${it.size}")
                    }
                    .doOnError {
                        Log.i(TAG, "Error ${it.message}")
                    }
            }
            else -> Single.just(currentNews)
        }


    private fun operationFetchAndPersNews(
        nextPage: Int
    ): Single<ArrayList<Article>> =
        when (networkConnection(context)) {
            true -> {
                delete().andThen(newsApiRemote.getNews(nextPage))
                    .flatMap {
                        when (it.status) {
                            StatusType.STATUS_OK.toLowerCase() -> {
                                Single.just(it)
                            }
                            else -> Single.error(FailureException())
                        }
                    }
                    .doAfterSuccess {
                        dataSource.saveNewsResult(it.totalResults)
                    }
                    .map {
                        it.articles
                    }
                    .doOnSuccess {
                        Log.i(TAG, "dispatch ${it.size} from API..")
                        persNews(it, nextPage)
                    }
            }
            else -> Single.error(NetworkException())
        }

    private fun checkOperatationFetchAndInsert(pageNumber: Int): Single<ArrayList<Article>> =
        dataSource.getNews()
            .flatMap {
                val newsListSize = it.size
                when (newsListSize) {
                    LIST_IS_EMPTY -> return@flatMap operationFetchAndPersNews(pageNumber)
                    else -> {
                        if (newsListSize == dataSource.getNewsResult()) {
                            return@flatMap Single.just(it)
                        } else {
                            return@flatMap mergeNews(it, pageNumber)
                        }
                    }
                }
            }

    @SuppressLint("CheckResult")
    private fun persNews(
        news: ArrayList<Article>,
        nextPage: Int
    ){
        insertNews(*ListUtil.toArray(Article::class.java,news))
            .andThen(dataSource.saveNewsPageNumber(nextPage))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success persisting news...") },
                { Log.e(TAG, "Failure persisting news...") })
    }

    private fun insertNews(vararg news: Article): Completable =
        dataSource.insertNews(*news)

    private fun delete(): Completable = dataSource.deleteNews()
        .doOnError { Log.e(TAG, "Failure deleting news...") }

    fun loadNews(): Boolean = dataSource.getloadNews()

    fun getNewsPageNumber(): Int = dataSource.getNewsPageNumber()

    fun saveLoadNews(shouldLoadMoreNews: Boolean) =
        dataSource.saveloadNews(shouldLoadMoreNews)
}