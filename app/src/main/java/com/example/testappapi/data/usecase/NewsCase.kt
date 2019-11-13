package com.example.testappapi.data.usecase

import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.repository.NewsRepository
import io.reactivex.Single
import javax.inject.Inject

class NewsCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    fun getNews(pageNumber: Int, refresh: Boolean): Single<ArrayList<Article>> =
        newsRepository.getNews(pageNumber, refresh)

    fun loadNews(): Boolean = newsRepository.loadNews()

    fun getNewsPageNumber(): Int = newsRepository.getNewsPageNumber()

    fun saveLoadNews(loadNews : Boolean) =
        newsRepository.saveLoadNews(loadNews)
}