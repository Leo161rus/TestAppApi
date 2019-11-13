package com.example.testappapi.presentor.sourcelist

import com.example.testappapi.R
import com.example.testappapi.common.addTo
import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.usecase.ArticleCase
import com.example.testappapi.presentor.ui.FragmentShortDescription
import com.example.testappapi.presentor.ui.Header
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ArticleListPresenter @Inject constructor(
    private val articleCase: ArticleCase
) : ArticleListContract.Presenter {

    private val loadNewsObserver = PublishSubject.create<Article>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: ArticleListContract.View

    private var pageNum: Int = 0
    private var isRefreshing: Boolean = false
    private lateinit var sourceId: String

    companion object {
        private const val TAG = "ArticleListPresenter"
    }

    override fun setView(
        articleList: ArticleListFragment,
        id: String
    ) {
        view = articleList
        sourceId = id

        setupLoadNewsChange()
        getArticleList(isRefreshing = false)
    }

    private fun setupLoadNewsChange(): Disposable =
        loadNewsObserver
            .doOnNext { articleCase.saveLoadArticles(true) }
            .subscribe { getArticleList(isRefreshing = false) }

    private fun getArticleList(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing

        if (!hasReachedResults()) {
            val disposable = articleCase.getArticles(sourceId, getPageNum())
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
        } else {
            view.showRefreshing(false)
        }
    }

    private fun hasReachedResults(): Boolean =
        articleCase.getArticlesPageNum() > 0 && (articleCase.getArticlesListSize() == articleCase.getArticlesResult())

    private fun setLoaders(isRefreshing: Boolean, showLoader: Boolean) = when (isRefreshing) {
        true -> view.showRefreshing(showLoader)
        else -> view.showLoader(showLoader)
    }

    private fun getPageNum(): Int =
        when (articleCase.loadArticles()) {
            true -> {
                articleCase.saveLoadArticles(false)
                pageNum = articleCase.getArticlesPageNum()
                ++pageNum
            }
            else -> {
                pageNum = articleCase.getArticlesPageNum()
                pageNum
            }
        }

    override fun refreshList() = getArticleList(isRefreshing = true)

    private fun onSuccess(articleList: ArrayList<Article>) {
        if (isRefreshing) view.clearArticleList()

        view.showArticleList(prepareListNewsList(articleList))
    }

    private fun prepareListNewsList(articleList: ArrayList<Article>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader = Header(R.string.articleHeader, R.layout.header)
        articleList.forEach {
            listItems.add(
                FragmentShortDescription(
                    listHeader,
                    it,
                    loadNewsObserver,
                    articleList.size
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) = view.showError(throwable.message)

    fun clearArticles() {
        view.clearArticleList()
        articleCase.clear()
    }

    fun destroy() = compositeDisposable.dispose()
}