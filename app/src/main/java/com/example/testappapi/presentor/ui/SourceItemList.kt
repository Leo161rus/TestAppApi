package com.example.testappapi.presentor.ui

import android.view.View
import com.example.testappapi.R
import com.example.testappapi.common.clickWithDebounce
import com.example.testappapi.data.model.sources.Source
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_source.*

class SourceItemList (
    header: Header,
    private val source: Source,
    private val sourceObserver: Subject<Source>
) : AbstractSectionableItem<SourceItemList.ViewHolder, Header>(header) {

    override fun getLayoutRes(): Int = R.layout.item_source

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): ViewHolder =
        ViewHolder(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: ViewHolder,
        pos: Int,
        payloads: List<Any>
    ) = holder.bind(
        source,
        sourceObserver
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            source: Source,
            sourceObserver: Subject<Source>
        ) {
            setSourceTitleText(source)
            setDescriptionText(source)
            setUrlText(source)

            setOpenArticleDetails(source, sourceObserver)
        }

        private fun setSourceTitleText(source: Source) = with(titleSourceText) {
            text = source.name
        }

        private fun setDescriptionText(source: Source) = with(descriptionText) {
            text = source.description
        }

        private fun setUrlText(source: Source) = with(urlText) {
            text = source.url
        }

        private fun setOpenArticleDetails(
            source: Source,
            sourceObserver: Subject<Source>
        ) = containerView?.clickWithDebounce {
            sourceObserver.onNext(source)
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is SourceItemList) source == other.source
        else false

    override fun hashCode(): Int {
        return source.hashCode()
    }
}