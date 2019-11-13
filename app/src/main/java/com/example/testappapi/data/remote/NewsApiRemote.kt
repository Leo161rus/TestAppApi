package com.example.testappapi.data.remote

import com.example.testappapi.data.model.news.NewsResponse
import com.example.testappapi.data.model.sources.Source
import io.reactivex.Single
import javax.inject.Inject

class NewsApiRemote @Inject constructor(private val newsApiService: NewsApiService) {

    fun getArticles(id : String, nextPage : Int): Single<NewsResponse> = newsApiService.getArticles(id,nextPage)

    fun getNews(nextPage: Int) : Single<NewsResponse> = newsApiService.getNews(nextPage)

    fun getSource() : Single<ArrayList<Source>> = newsApiService.getSources().map { it.sources }

}