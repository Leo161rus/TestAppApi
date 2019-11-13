package com.example.testappapi.presentor.sourcelist

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface ArticleListContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showArticleList(articleList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun clearArticleList()
        fun showRefreshing(show: Boolean)
    }

    interface Presenter {
        fun refreshList()
        fun setView(
            articleList: ArticleListFragment,
            id: String
        )
    }
}