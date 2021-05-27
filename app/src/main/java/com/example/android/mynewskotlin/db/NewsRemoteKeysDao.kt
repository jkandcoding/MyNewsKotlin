package com.example.android.mynewskotlin.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<NewsRemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE title = :title")
    suspend fun remoteKeyByTitle(title: String): NewsRemoteKeys

    @Query("SELECT * FROM remote_keys ORDER BY nextKey DESC LIMIT 1")
    suspend fun remoteKeysForLastEntry(): NewsRemoteKeys

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeysDb()
}