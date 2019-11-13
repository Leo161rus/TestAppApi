package com.example.testappapi.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.example.testappapi.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

private fun getDateFormat() : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

private fun getModificationDate() : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun modifyDate(publishedAt : String) : String{
    val date = getModificationDate().parse(publishedAt)
    return getDateFormat().format(date)
}

fun Disposable?.addTo(compositeDisposable : CompositeDisposable){
    this?.also { compositeDisposable.add(it) }
}

fun setImage(imageView: ImageView, path: String?) {
    GlideApp.with(imageView)
        .load(path)
        .apply(RequestOptions().circleCrop())
        .apply(RequestOptions().placeholder(R.drawable.ic_newsplaceholder))
        .into(imageView)
}

fun networkConnection(context: Context) : Boolean{
    var isConnected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (networkInfo != null && networkInfo.isConnected)
        isConnected = true
    return isConnected
}