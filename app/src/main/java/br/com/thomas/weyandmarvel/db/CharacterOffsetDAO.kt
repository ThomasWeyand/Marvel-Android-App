package br.com.thomas.weyandmarvel.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterOffsetDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterOffset>)

    @Query("SELECT * FROM characterOffset WHERE id = :consultID")
    suspend fun getOffsetById(consultID: Long): CharacterOffset?

    @Query("DELETE FROM characterOffset")
    suspend fun clearRemoteOffset()

}