package com.example.testappapi.presentor.sources

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.testappapi.NewsAppApi
import com.example.testappapi.R
import com.example.testappapi.common.gone
import com.example.testappapi.common.inflate
import com.example.testappapi.common.visible
import com.example.testappapi.data.model.sources.Source
import com.example.testappapi.data.usecase.SourceCase
import com.example.testappapi.presentor.MainActivity
import com.example.testappapi.presentor.sourcelist.ArticleListFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_source.*
import javax.inject.Inject

class SourcesFragment : Fragment(), SourcesContract.View {

    @Inject lateinit var sourceCase : SourceCase

    @InjectPresenter lateinit var presenter: SourcesPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>
    private lateinit var newsAppApi: NewsAppApi

    @ProvidePresenter fun provideSourcePresenter() : SourcesPresenter{
        return SourcesPresenter(sourceCase = sourceCase)
    }

    companion object {
        const val TITLE = "Sources"
        private const val TAG = "SourcesFragment"
        const val KEY = "Source"
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
        container?.inflate(R.layout.fragment_source)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        presenter.setView(this)
    }

    private fun setupUI() {
        (activity as MainActivity).showBackButton(false)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        sourcesList.adapter = adapter
        sourcesList.layoutManager = LinearLayoutManager(context)
        sourcesList.isNestedScrollingEnabled = true
    }

    private fun errorMsg() {
        sourcesList.gone()
        errorText.visible()
    }

    override fun onSourceClicked(source: Source) {
        val bundle = Bundle()
        bundle.putParcelable(KEY, source)

        val fragment = ArticleListFragment()
        fragment.arguments = bundle

        (context as MainActivity)
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.main_container, fragment, MainActivity.FRAGMENT_KEY)
            .addToBackStack(null)
            .commit()
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        when (show) {
            true -> visible()
            else -> gone()
        }
    }

    override fun showSources(sourcesList: List<AbstractFlexibleItem<*>>) =
        adapter.updateDataSet(sourcesList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
            errorMsg()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }
}