package com.example.testappapi.presentor.ui

import android.view.View
import com.example.testappapi.common.gone
import com.example.testappapi.common.modifyDate
import com.example.testappapi.common.setImage
import com.example.testappapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.subjects.Subject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_short_description_news.*

class FragmentShortDescription (
    header: Header,
    private val article: Article,
    private val loadObserver: Subject<Article>,
    private val listSize: Int
) : AbstractSectionableItem<FragmentShortDescription.ViewHolder, Header>(header) {

    override fun getLayoutRes(): Int = com.example.testappapi.R.layout.fragment_short_description_news

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
        article,
        loadObserver,
        pos,
        listSize
    )

    class ViewHolder(override var containerView: View?, adapter: FlexibleAdapter<*>) :
        FlexibleViewHolder(containerView, adapter), LayoutContainer {

        fun bind(
            article: Article,
            loadNewsObserver: Subject<Article>,
            pos: Int,
            listSize: Int
        ) {
            setImage(article)
            setTitle(article)
            setDescription(article)
            setAuthor(article)
            setPublished(article)

            setLoadNews(pos, listSize, loadNewsObserver, article)
        }

        private fun setImage(article: Article) = with(image) {
            article.urlToImage?.let {
                setImage(this, it)
            }
        }

        private fun setLoadNews(
            position: Int,
            listSize: Int,
            loadMoreNewsObserver: Subject<Article>,
            article: Article
        ) {
            if (position == listSize)
                loadMoreNewsObserver.onNext(article)
        }

        private fun setTitle(article: Article) = with(titleText) {
            text = article.title
        }

        private fun setDescription(article: Article) = with(descriptionText) {
            article.description?.let {
                text = article.description
            } ?: gone()
        }

        private fun setAuthor(article: Article) = with(authorText) {
            article.author?.let {
                text = it
            }
        }

        private fun setPublished(article: Article) = with(publishedText) {
            try {
                text = modifyDate(article.publishedAt)
            } catch (e: Exception) {
                gone()
            }
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is FragmentShortDescription) listSize == other.listSize
        else false

    override fun hashCode(): Int = listSize
}