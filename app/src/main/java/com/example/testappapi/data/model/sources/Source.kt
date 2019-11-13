package com.example.testappapi.data.model.sources

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class Source(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val name: String,
    val category: String,
    val country: String,
    val language: String,
    val description: String,
    val url: String
) : Parcelable {
    constructor(p: Parcel) : this(
        p.readString()!!,
        p.readString()!!,
        p.readString()!!,
        p.readString()!!,
        p.readString()!!,
        p.readString()!!,
        p.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p: Parcel, flags: Int) {
        p.writeString(id)
        p.writeString(name)
        p.writeString(category)
        p.writeString(country)
        p.writeString(language)
        p.writeString(description)
        p.writeString(url)
    }

    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(p: Parcel): Source = Source(p)

        override fun newArray(size: Int): Array<Source?> = arrayOfNulls(size)
    }
}