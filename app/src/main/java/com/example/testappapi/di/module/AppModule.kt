package com.example.testappapi.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.testappapi.data.local.PaginationArticlesRepository
import com.example.testappapi.data.local.PaginationNewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    private val NEWS_API_SHARED_PREF = "NEWS_API_SHARED_PREF"

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(NEWS_API_SHARED_PREF, Context.MODE_PRIVATE)

    @Provides
    fun providesPaginationNewsRepository(context: Context): PaginationNewsRepository =
        PaginationNewsRepository(context)

    @Provides
    fun providesPaginationArticlesRepository(context: Context): PaginationArticlesRepository =
        PaginationArticlesRepository(context)
}