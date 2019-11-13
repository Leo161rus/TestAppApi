package com.example.testappapi.di.module

import android.content.Context
import androidx.room.Room
import com.example.testappapi.data.local.ApiDatabase
import com.example.testappapi.data.local.NewsDao
import com.example.testappapi.data.local.SourceDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideNewsDao(database : ApiDatabase) : NewsDao = database.newsDao()

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : ApiDatabase =
        Room.databaseBuilder(context,ApiDatabase :: class.java, "NewsApi.db").build()

    @Provides
    @Singleton
    fun provideSourcesDao(database: ApiDatabase) : SourceDao = database.sourcesDao()
}