package com.example.testappapi.presentor.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.testappapi.NewsAppApi
import com.example.testappapi.common.gone
import com.example.testappapi.common.inflate
import com.example.testappapi.common.visible
import com.example.testappapi.data.usecase.NewsCase
import com.example.testappapi.presentor.MainActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_short_description_news.*
import kotlinx.android.synthetic.main.fragment_short_description_news.errorText
import kotlinx.android.synthetic.main.fragment_source.*
import javax.inject.Inject

class NewsFragment : Fragment(), NewsContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var newsCase : NewsCase

    @InjectPresenter lateinit var presenter: NewsPresenter

    @ProvidePresenter
    fun provideNewsPresenter() : NewsPresenter{
        return NewsPresenter(newsCase = newsCase)
    }
    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>
    private lateinit var newsAppApi: NewsAppApi

    companion object {
        const val TITLE = "News"
        private const val TAG = "NewsFragment"
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
        presenter.setView(this)
    }

    private fun showErrorMessage() {
        fragment_short_description_List.gone()
        errorText.visible()
    }

    private fun setupUI() {
        swipeToRefresh.setOnRefreshListener(this)
        (activity as MainActivity).showBackButton(false)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        fragment_short_description_List.adapter = adapter
        fragment_short_description_List.layoutManager = LinearLayoutManager(context)
        fragment_short_description_List.isNestedScrollingEnabled = true
    }

    override fun onRefresh() = presenter.refreshList()

    override fun onPause() {
        super.onPause()

        if (progressBarLayout.isVisible) progressBarLayout.gone()

        if (swipeToRefresh.isRefreshing) swipeToRefresh.gone()
    }

    override fun showRefreshing(show: Boolean) = with(swipeToRefresh) {
        this?.let {
            isRefreshing = show
        }
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        this?.let {
            when (show) {
                true -> visible()
                else -> gone()
            }
        }
    }

    override fun clearNewsList() = adapter.clear()

    override fun showNews(newsList: List<AbstractFlexibleItem<*>>) = adapter.updateDataSet(newsList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
            showErrorMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }
}