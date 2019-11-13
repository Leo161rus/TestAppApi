package com.example.testappapi.presentor.news

import android.preference.PreferenceActivity
import android.util.Log
import com.example.testappapi.R
import com.example.testappapi.common.addTo
import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.usecase.NewsCase
import com.example.testappapi.presentor.ui.FragmentShortDescription
import com.example.testappapi.presentor.ui.Header
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    private val newsCase: NewsCase
) : NewsContract.Presenter {

    private val loadNewsObserver = PublishSubject.create<Article>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: NewsContract.View

    private var pageNum: Int = 0
    private var isRefreshing: Boolean = false

    companion object {
        private const val TAG = "NewsPresenter"
    }

    override fun setView(newsFragment: NewsFragment) {
        view = newsFragment
        setupLoadNewsChange()
        getNews(isRefreshing = false)
    }

    private fun setupLoadNewsChange(): Disposable =
        loadNewsObserver
            .doOnNext { newsCase.saveLoadNews(loadNews = true) }
            .subscribe(
                { getNews(isRefreshing = false) },
                { Log.e(TAG, "Error: $it") })

    private fun getNews(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing

        val disposable = newsCase.getNews(getPageNumber(), isRefreshing)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                setLoaders(isRefreshing, showLoader = true)
            }
            .doAfterTerminate {
                setLoaders(isRefreshing, showLoader = false)
            }
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    private fun setLoaders(isRefreshing: Boolean, showLoader: Boolean) = when (isRefreshing) {
        true -> view.showRefreshing(showLoader)
        else -> view.showLoader(showLoader)
    }

    private fun getPageNumber(): Int =
        when (newsCase.loadNews()) {
            true -> {
                newsCase.saveLoadNews(loadNews = false)
                pageNum = newsCase.getNewsPageNumber()
                ++pageNum
            }
            else -> {
                pageNum = newsCase.getNewsPageNumber()
                pageNum
            }
        }

    override fun refreshList() = getNews(isRefreshing = true)

    private fun onSuccess(newsList: ArrayList<Article>) {
        if (isRefreshing) view.clearNewsList()

        view.showNews(prepareListNewsList(newsList))
    }

    private fun prepareListNewsList(newsList: ArrayList<Article>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader =
           Header (R.string.newsHeader, R.layout.header)
        newsList.forEach {
            listItems.add(
                FragmentShortDescription(
                    listHeader,
                    it,
                    loadNewsObserver,
                    newsList.size
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) = view.showError(throwable.message)

    fun destroy() = compositeDisposable.dispose()
}