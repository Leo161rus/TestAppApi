package com.example.testappapi

import android.annotation.SuppressLint
import android.app.Application
import com.example.testappapi.di.component.AppComponent
import com.example.testappapi.di.component.DaggerAppComponent
import com.example.testappapi.di.module.AppModule
import com.example.testappapi.di.module.RestModule
import com.example.testappapi.di.module.RoomModule


@SuppressLint("Registered")
class NewsAppApi : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule())
            .restModule(RestModule(this))
            .roomModule(RoomModule())
            .build()

        appComponent.inject(this)
    }

}