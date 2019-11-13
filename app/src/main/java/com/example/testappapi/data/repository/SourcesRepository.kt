package com.example.testappapi.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.testappapi.common.ListUtil
import com.example.testappapi.common.networkConnection
import com.example.testappapi.data.NetworkException
import com.example.testappapi.data.local.DataSource
import com.example.testappapi.data.model.sources.Source
import com.example.testappapi.data.remote.NewsApiRemote
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SourcesRepository @Inject constructor(
    private val newsApiRemote: NewsApiRemote,
    private val dataSource: DataSource,
    private val context: Context
) {

    companion object {
        private const val TAG = "SourcesRepository"
    }

    fun getSources(): Single<ArrayList<Source>> =
        dataSource.getSources()
            .flatMap {
                if (it.isNotEmpty()) {
                    Log.i(TAG, "Dispatching ${it.size} sources from DB...")
                    return@flatMap Single.just(it)
                } else {
                    return@flatMap operationFetchAndPersSources()
                }
            }

    @SuppressLint("CheckResult")
    private fun operationFetchAndPersSources(): Single<ArrayList<Source>> =
        when (networkConnection(context)) {
            true -> {
                newsApiRemote.getSource()
                    .doOnSuccess {
                        persistSources(it)
                    }
                    .doOnError {
                        Log.i(TAG, "Error ${it.message}")
                    }
            }
            false -> Single.error(NetworkException())
        }

    @SuppressLint("CheckResult")
    private fun persistSources(sources: ArrayList<Source>) {
        insertSources(*ListUtil.toArray(Source::class.java, sources))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success persisting sources...") },
                { Log.e(TAG, "Failure persisting sources...") })
    }

    private fun insertSources(vararg sources: Source): Completable =
        dataSource.insertSource(*sources)
}