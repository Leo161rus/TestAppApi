package com.example.testappapi.data.repository

import android.content.Context
import android.util.Log
import com.example.testappapi.common.networkConnection
import com.example.testappapi.data.FailureException
import com.example.testappapi.data.NetworkException
import com.example.testappapi.data.local.DataSource
import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.model.statusType.StatusType
import com.example.testappapi.data.remote.NewsApiRemote
import io.reactivex.Single
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val context: Context,
    private val newsApiRemote: NewsApiRemote,
    private val dataSource: DataSource
){

    companion object{
        private const val TAG = "ArticleRepository"
    }

    fun getArticles(id : String, nextPage : Int) : Single<ArrayList<Article>> =
        when(networkConnection(context)){
            true -> {
                newsApiRemote.getArticles(id,nextPage)
                    .flatMap {
                        when(it.status){
                            StatusType.STATUS_OK.toLowerCase() -> Single.just(it)
                            else -> Single.error(FailureException())
                        }
                    }
                    .doAfterSuccess{ dataSource.saveArticlesResult(it.totalResults)}
                    .map { it.articles }
                    .doOnSuccess {
                        dataSource.saveArticlesListSize(it.size)
                        dataSource.saveArticlesPageNumber(nextPage)
                    }
                    .doOnError{
                        Log.i(TAG,"Error ${it.message}")
                    }
            }
            else -> Single.error(NetworkException())
        }

    fun loadArticles(): Boolean = dataSource.getloadArticles()

    fun getArticlesPageNumber(): Int = dataSource.getArticlesPageNumber()

    fun saveLoadArticles(loadArticles: Boolean) =
        dataSource.saveLoadArticles(loadArticles)

    fun getArticlesResult(): Int = dataSource.getArticlesResult()

    fun getArticlesListSize(): Int = dataSource.getArticlesListSize()

    fun clearArticles() = dataSource.clearArticlesRepository()

}