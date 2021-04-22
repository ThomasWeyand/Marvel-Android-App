package br.com.thomas.weyandmarvel.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characterOffset")
data class CharacterOffset(
    @PrimaryKey val id: Long,
    val prevOffset: Int?,
    val nextOffset: Int?
)