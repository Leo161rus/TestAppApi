package com.example.testappapi.di.component

import com.example.testappapi.NewsAppApi
import com.example.testappapi.data.remote.NewsApiService
import com.example.testappapi.presentor.MainActivity
import com.example.testappapi.di.module.AppModule
import com.example.testappapi.di.module.RestModule
import com.example.testappapi.di.module.RoomModule
import com.example.testappapi.presentor.news.NewsFragment
import com.example.testappapi.presentor.sourcelist.ArticleListFragment
import com.example.testappapi.presentor.sources.SourcesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule :: class, AppModule :: class, RestModule :: class])
interface AppComponent {

    fun apiService(): NewsApiService

    fun inject(application: NewsAppApi)

    fun inject(activity: MainActivity)

    fun inject(fragment: SourcesFragment)

    fun inject(fragment: NewsFragment)

    fun inject(fragment: ArticleListFragment)

}