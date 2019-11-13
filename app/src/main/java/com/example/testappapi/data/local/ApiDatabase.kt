package com.example.testappapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testappapi.data.model.news.Article
import com.example.testappapi.data.model.sources.Source

@Database(entities = [Article::class, Source::class], version = 1, exportSchema = false)
@TypeConverters(Article.ArticleFromSource::class)
abstract class ApiDatabase : RoomDatabase() {
    abstract fun sourcesDao(): SourceDao
    abstract fun newsDao(): NewsDao
}