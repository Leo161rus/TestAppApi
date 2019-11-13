package com.example.testappapi.presentor.ui

import android.content.Context
import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.header.*

class Header (
    private val strHeaderId: Int,
    private val layoutHeaderId: Int
) :
    AbstractHeaderItem<Header.ViewHolder>() {

    override fun equals(other: Any?): Boolean {
        if (other is Header) return this.strHeaderId == other.strHeaderId
        return false
    }

    override fun getLayoutRes(): Int = layoutHeaderId

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): ViewHolder =
        ViewHolder(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: ViewHolder,
        position: Int,
        payloads: List<Any>
    ) = holder.bind(strHeaderId)

    override fun hashCode(): Int = strHeaderId.hashCode()

    class ViewHolder(override val containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter, true), LayoutContainer {

        private val context: Context = itemView.context

        fun bind(strHeaderId: Int) {
            header.text = context.getString(strHeaderId)
        }
    }
}