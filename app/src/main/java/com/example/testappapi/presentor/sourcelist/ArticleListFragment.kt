package com.example.testappapi.presentor.sourcelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.testappapi.NewsAppApi
import com.example.testappapi.common.gone
import com.example.testappapi.common.inflate
import com.example.testappapi.common.visible
import com.example.testappapi.data.model.sources.Source
import com.example.testappapi.data.usecase.ArticleCase
import com.example.testappapi.presentor.MainActivity
import com.example.testappapi.presentor.sources.SourcesFragment
import com.example.testappapi.presentor.sources.SourcesPresenter
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_short_description_news.*
import kotlinx.android.synthetic.main.fragment_short_description_news.errorText
import kotlinx.android.synthetic.main.fragment_source.*
import javax.inject.Inject

class ArticleListFragment : Fragment(), ArticleListContract.View,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var articleCase : ArticleCase

    @InjectPresenter lateinit var presenter: ArticleListPresenter

    @ProvidePresenter
    fun provideArticleListFragment() : ArticleListPresenter {
        return ArticleListPresenter(articleCase = articleCase)
    }

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>
    private lateinit var newsAppApi: NewsAppApi

    companion object {
        private const val TAG = "SourcesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsAppApi.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        container?.inflate(com.example.testappapi.R.layout.fragment_short_description_news)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        val source = arguments?.getParcelable<Source>(SourcesFragment.KEY)
        source?.id?.let { presenter.setView(this, it) } ?: showErrorMessage()
    }

    private fun showErrorMessage() {
        fragment_short_description_List.gone()
        errorText.visible()
    }

    private fun setupUI() {
        swipeToRefresh.setOnRefreshListener(this)
        (activity as MainActivity).showBackButton(true)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        fragment_short_description_List.adapter = adapter
        fragment_short_description_List.layoutManager = LinearLayoutManager(context)
        fragment_short_description_List.isNestedScrollingEnabled = true
    }

    override fun onRefresh() = presenter.refreshList()

    override fun onStop() {
        super.onStop()

        presenter.clearArticles()
    }

    override fun showRefreshing(show: Boolean) = with(swipeToRefresh) {
        isRefreshing = show
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        this.let {
            when (show) {
                true -> visible()
                else -> gone()
            }
        }
    }

    override fun clearArticleList() = adapter.clear()

    override fun showArticleList(articleList: List<AbstractFlexibleItem<*>>) =
        adapter.updateDataSet(articleList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
            showErrorMessage()
        }
    }

    override fun onDetach() {
        (activity as MainActivity).showBackButton(false)
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }
}