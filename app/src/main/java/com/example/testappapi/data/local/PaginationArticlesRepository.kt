package com.example.testappapi.data.local

import android.content.Context
import javax.inject.Singleton

@Singleton
class PaginationArticlesRepository (context : Context) :
        SharedPreferencesRepository(
            context.getSharedPreferences(
                ARTICLES_PAGINATION,
                Context.MODE_PRIVATE
            )
        ){

    companion object {
        private const val ARTICLES_PAGINATION = "com.example.testappapi.ARTICLES_PAGINATION"
        private const val ARTICLES_PAGE_ID = "ARTICLES_PAGE_ID"
        private const val ARTICLES_LIST_SIZE = "ARTICLES_LIST_SIZE"
        private const val ARTICLES_RESULTS = "ARTICLE_RESULTS"
        private const val LOAD_ARTICLES = "LOAD_ARTICLES"
    }

    fun getArticlesPageNumber() : Int {
        return sharedPreference.getInt(ARTICLES_PAGE_ID,0)
    }

    fun loadArticles() : Boolean{
        return sharedPreference.getBoolean(ARTICLES_RESULTS,true)
    }

    fun getArticlesListSize() : Int {
        return sharedPreference.getInt(ARTICLES_LIST_SIZE,0)
    }

    fun getArticlesResults() : Int {
        return sharedPreference.getInt(ARTICLES_RESULTS,0)
    }

    fun putLoadArticles(loadArticles : Boolean){
        val editor = sharedPreference.edit()
        editor.putBoolean(LOAD_ARTICLES, loadArticles).apply()
    }

    fun putArticlesPageNumber(num : Int){
        val editor = sharedPreference.edit()
        editor.putInt(ARTICLES_PAGE_ID, num).apply()
    }

    fun putArticlesResults(totalResults : Int){
        val editor = sharedPreference.edit()
        editor.putInt(ARTICLES_RESULTS,totalResults).apply()
    }

    fun putArticlesListSize(articleListSize : Int){
        val editor = sharedPreference.edit()
        editor.putInt(ARTICLES_LIST_SIZE,articleListSize).apply()
    }

    fun clear() {
        val editor = sharedPreference.edit()
        editor.remove(ARTICLES_PAGINATION)
        editor.remove(ARTICLES_PAGE_ID)
        editor.remove(ARTICLES_LIST_SIZE)
        editor.remove(ARTICLES_RESULTS)
        editor.remove(LOAD_ARTICLES)
        editor.apply()
    }
}