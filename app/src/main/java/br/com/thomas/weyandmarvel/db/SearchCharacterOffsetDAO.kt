package br.com.thomas.weyandmarvel.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchCharacterOffsetDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<SearchCharacterOffset>)

    @Query("SELECT * FROM searchCharacterOffset WHERE id = :consultID")
    suspend fun getOffsetById(consultID: Long): SearchCharacterOffset?

    @Query("DELETE FROM searchCharacterOffset")
    suspend fun clearRemoteOffset()

}