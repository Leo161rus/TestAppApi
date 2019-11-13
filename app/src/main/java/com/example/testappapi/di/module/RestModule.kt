package com.example.testappapi.di.module

import android.content.Context
import com.example.testappapi.BuildConfig
import com.example.testappapi.NewsAppApi
import com.example.testappapi.common.networkConnection
import com.example.testappapi.data.remote.NewsApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RestModule (private val application: NewsAppApi) {

    companion object {
        const val API_POINT = "https://newsapi.org/v2/"
    }

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideBaseUrl(): String = API_POINT

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(context: Context): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.APPLICATION_ID)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        var request = requestBuilder.build()

        request = if (networkConnection(context))
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        else
            request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()

        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor, context: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .cache(myCache)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(baseUrl: String, gson: Gson, okHttpClient: OkHttpClient): NewsApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create<NewsApiService>(NewsApiService::class.java)
}