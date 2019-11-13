package com.example.testappapi.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PaginationNewsRepository (context: Context) :
        SharedPreferencesRepository(context.getSharedPreferences(
            NEWS_PAGINATION,
            Context.MODE_PRIVATE
        )
        ) {

    companion object{
        private const val NEWS_PAGINATION = "com.example.appapitest.NEWS_PAGINATION"
        private const val NEWS_PAGE_ID = "NEWS_PAGE_ID"
        private const val LOAD_NEWS = "LOAD_NEWS"
        private const val NEWS_RESULTS = "NEWS_RESULTS"
    }

    fun getNewsPageNumber() : Int {
        return sharedPreference.getInt(NEWS_PAGE_ID,0)
    }

    fun loadNews() : Boolean {
        return sharedPreference.getBoolean(LOAD_NEWS, false)
    }

    fun getNewsResults() : Int {
        return sharedPreference.getInt(NEWS_RESULTS, 0)
    }

    @SuppressLint("CommitPrefEdits")
    fun putNewsPageNumber(num : Int){
        val editor = sharedPreference.edit()
        editor.putInt(NEWS_PAGE_ID,num)
    }

    @SuppressLint("CommitPrefEdits")
    fun putLoadNews(loadNews : Boolean){
        val editor = sharedPreference.edit()
        editor.putBoolean(LOAD_NEWS,loadNews)
    }

    @SuppressLint("CommitPrefEdits")
    fun putNewsResults(newsResult : Int){
        val editor = sharedPreference.edit()
        editor.putInt(NEWS_RESULTS,newsResult)
    }

}