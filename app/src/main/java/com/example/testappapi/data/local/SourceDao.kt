package com.example.testappapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testappapi.data.model.sources.Source
import io.reactivex.Single

@Dao
interface SourceDao {
    @Query("SELECT * from sources")
    fun getSources() : Single<List<Source>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(vararg sources : Source)
}