package com.example.testappapi.common

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false) : View =
    LayoutInflater.from(context).inflate(layoutId,this,attachToRoot)

fun View.clickWithDebounce(debounceTime: Long = 800L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastTimeClick: Long = 0
        override fun onClick(v: View) {
            val differenceTime = SystemClock.elapsedRealtime() - lastTimeClick
            when {
                differenceTime < debounceTime -> return
                else -> action()
            }
            lastTimeClick = SystemClock.elapsedRealtime()
        }
    })
}