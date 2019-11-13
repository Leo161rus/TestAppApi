package com.example.testappapi.data.model.news

import android.os.Parcel
import android.os.Parcelable

class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<Article>
) : Parcelable {
    constructor(p: Parcel) : this(
        p.readString()!!,
        p.readInt(),
        arrayListOf<Article>().apply {
            p.readList(this, Article::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeInt(totalResults)
        parcel.writeTypedList(articles)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NewsResponse> {
        override fun createFromParcel(p: Parcel): NewsResponse =
            NewsResponse(p)

        override fun newArray(size: Int): Array<NewsResponse?> = arrayOfNulls(size)
    }
}