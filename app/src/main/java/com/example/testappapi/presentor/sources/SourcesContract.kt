package com.example.testappapi.presentor.sources

import com.example.testappapi.data.model.sources.Source
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface SourcesContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showSources(sourcesList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun onSourceClicked(source: Source)
    }

    interface Presenter {
        fun setView(sourcesFragment: SourcesFragment)
    }
}