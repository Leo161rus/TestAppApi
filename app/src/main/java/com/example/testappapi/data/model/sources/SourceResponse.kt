package com.example.testappapi.data.model.sources

import android.os.Parcel
import android.os.Parcelable

data class SourceResponse(
    val status: String,
    val sources: ArrayList<Source>
) : Parcelable {
    constructor(p: Parcel) : this(
        p.readString()!!,
        arrayListOf<Source>().apply {
            p.readList(this, Source::class.java.classLoader)
        }
    )

    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(p: Parcel, flags: Int) {
        p.writeString(status)
        p.writeTypedList(sources)
    }


    companion object CREATOR : Parcelable.Creator<SourceResponse> {
        override fun createFromParcel(p: Parcel): SourceResponse {
            return SourceResponse(p)
        }

        override fun newArray(size: Int): Array<SourceResponse?> {
            return arrayOfNulls(size)
        }
    }
}