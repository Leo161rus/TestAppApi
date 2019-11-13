package com.example.testappapi.data.local

import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class DataSource @Inject constructor(
    private val sourceDao : SourceDao,
    private val newsDao: NewsDao,
    private val paginationArticlesRepository: PaginationArticlesRepository,
    private val paginationNewsRepository: PaginationNewsRepository
){

    fun getSources() : Single<ArrayList<Source>> = sourceDao.getSources().map { ArrayList(it) }

    fun insertSource(vararg source : Source) : Completable = Completable.fromAction { sourceDao.insertSources(*source) }

    fun getNews() : Single<ArrayList<Article>> = newsDao.getNews().map { ArrayList(it) }

    fun insertNews(vararg news: Article): Completable =
        Completable.fromAction { newsDao.insertNews(news as Array<Article>) }

    fun deleteNews(): Completable = Completable.fromAction { newsDao.deleteNews() }

    fun getloadNews(): Boolean = paginationNewsRepository.loadNews()

    fun saveloadNews(shouldLoadMore: Boolean) =
        paginationNewsRepository.putLoadNews(shouldLoadMore)

    fun getNewsPageNumber(): Int = paginationNewsRepository.getNewsPageNumber()

    fun saveNewsPageNumber(nextPage: Int): Completable =
        Completable.fromAction { paginationNewsRepository.putNewsPageNumber(nextPage) }

    fun saveNewsResult(newsResult: Int) =
        paginationNewsRepository.putNewsResults(newsResult)

    fun getNewsResult() = paginationNewsRepository.getNewsResults()

    fun getloadArticles(): Boolean = paginationArticlesRepository.loadArticles()

    fun saveLoadArticles(loadArticles: Boolean) =
        paginationArticlesRepository.putLoadArticles(loadArticles)

    fun getArticlesPageNumber(): Int = paginationArticlesRepository.getArticlesPageNumber()

    fun saveArticlesPageNumber(nextPage: Int) =
        paginationArticlesRepository.putArticlesPageNumber(nextPage)

    fun saveArticlesResult(articlesResults: Int) =
        paginationArticlesRepository.putArticlesResults(articlesResults)

    fun getArticlesResult() = paginationArticlesRepository.getArticlesResults()

    fun clearArticlesRepository() = paginationArticlesRepository.clear()

    fun getArticlesListSize() = paginationArticlesRepository.getArticlesListSize()

    fun saveArticlesListSize(articleListSize: Int) =
        paginationArticlesRepository.putArticlesListSize(articleListSize)
}