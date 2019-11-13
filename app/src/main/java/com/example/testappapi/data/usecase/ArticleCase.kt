package com.example.testappapi.data.usecase

import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.repository.ArticleRepository
import io.reactivex.Single
import javax.inject.Inject

class ArticleCase @Inject constructor(
    private val articleRepository: ArticleRepository
){

    fun getArticles(id: String, pageNumber: Int): Single<ArrayList<Article>> =
        articleRepository.getArticles(id, pageNumber)

    fun loadArticles(): Boolean = articleRepository.loadArticles()

    fun getArticlesPageNum(): Int = articleRepository.getArticlesPageNumber()

    fun saveLoadArticles(shouldLoadMoreArticles: Boolean) =
        articleRepository.saveLoadArticles(shouldLoadMoreArticles)

    fun getArticlesResult(): Int = articleRepository.getArticlesResult()

    fun getArticlesListSize(): Int = articleRepository.getArticlesListSize()

    fun clear() = articleRepository.clearArticles()
}