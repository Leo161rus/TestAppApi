package com.example.testappapi.data.remote

import com.example.testappapi.data.model.news.NewsResponse
import com.example.testappapi.data.model.sources.SourceResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    companion object{
        const val GET_SOURCES = "sources"
        const val GET_NEWS = "headlines"
    }

    @GET(GET_SOURCES)
    fun getSources(): Single<SourceResponse>

    @GET(GET_NEWS)
    fun getArticles(@Query("sources") source: String, @Query("page") page: Int): Single<NewsResponse>

    @GET(GET_NEWS)
    fun getNews(@Query("page") page: Int, @Query("language") language: String = "en"): Single<NewsResponse>
}