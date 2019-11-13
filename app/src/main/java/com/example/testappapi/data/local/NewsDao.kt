package com.example.testappapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testappapi.data.model.news.Article
import io.reactivex.Single

@Dao
interface NewsDao {
    @Query("SELECT * from newsFromSource")
    fun getNews(): Single<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: Array<Article>)

    @Query("DELETE FROM newsFromSource")
    fun deleteNews()
}