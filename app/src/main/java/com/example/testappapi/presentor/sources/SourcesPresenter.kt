package com.example.testappapi.presentor.sources

import android.util.Log
import com.example.testappapi.R
import com.example.testappapi.common.addTo
import com.example.testappapi.data.model.sources.Source
import com.example.testappapi.data.usecase.SourceCase
import com.example.testappapi.presentor.ui.Header
import com.example.testappapi.presentor.ui.SourceItemList
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SourcesPresenter @Inject constructor(
    private val sourceCase: SourceCase
) : SourcesContract.Presenter {

    private val sourceObserver = PublishSubject.create<Source>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: SourcesContract.View

    companion object {
        private const val TAG = "SourcesPresenter"
    }

    override fun setView(sourcesFragment: SourcesFragment) {
        view = sourcesFragment
        setupSourceChanged()
        getSources()
    }

    private fun setupSourceChanged(): Disposable =
        sourceObserver
            .subscribe(
                { view.onSourceClicked(it) },
                { Log.e(TAG, "Error: $it") })

    private fun getSources() {
        val disposable = sourceCase.getSource()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showLoader(true)
            }
            .doAfterTerminate {
                view.showLoader(false)
            }
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    private fun onSuccess(sourceList: ArrayList<Source>) = view.showSources(prepareNewsList(sourceList))

    private fun prepareNewsList(sourceList: ArrayList<Source>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader = Header(R.string.sourcesHeader, R.layout.header)

        sourceList.forEach { listItems.add(SourceItemList(listHeader, it, sourceObserver)) }

        return listItems
    }

    private fun onError(throwable: Throwable) = view.showError(throwable.message)

    fun destroy() = compositeDisposable.dispose()
}