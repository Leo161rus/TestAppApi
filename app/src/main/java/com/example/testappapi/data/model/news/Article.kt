package com.example.testappapi.data.model.news

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "newsFromSource")
data class Article(
    val source: ArticleFromSource,
    val author: String?,
    val title: String,
    val description: String? = "",
    val url: String,
    val urlToImage: String?,
    @PrimaryKey @ColumnInfo(name = "id") val publishedAt: String
) : Parcelable {
    constructor(p: Parcel) : this(
        ArticleFromSource.dataFromString(
            p.readString()!!),
            p.readString(),
            p.readString()!!,
            p.readString(),
            p.readString()!!,
            p.readString(),
            p.readString()!!
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(p: Parcel, flags: Int) {
        p.writeString(ArticleFromSource.dataToString(source))
        p.writeString(author)
        p.writeString(title)
        p.writeString(description)
        p.writeString(url)
        p.writeString(urlToImage)
        p.writeString(publishedAt)
    }

    class ArticleFromSource {

        lateinit var id: String
        lateinit var name: String

        companion object {

            @JvmStatic
            @TypeConverter
            fun dataToString(data: ArticleFromSource): String = Gson().toJson(data)

            @JvmStatic
            @TypeConverter
            fun dataFromString(dataJson: String): ArticleFromSource =
                Gson().fromJson(dataJson, ArticleFromSource::class.java)
        }
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(p: Parcel): Article =
            Article(p)

        override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
    }
}

