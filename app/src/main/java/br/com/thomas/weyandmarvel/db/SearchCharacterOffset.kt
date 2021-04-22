package br.com.thomas.weyandmarvel.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchCharacterOffset")
data class SearchCharacterOffset(
    @PrimaryKey val id: Long,
    val prevOffset: Int?,
    val nextOffset: Int?
)