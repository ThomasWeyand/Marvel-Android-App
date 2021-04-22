package br.com.thomas.weyandmarvel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class ResultItem {

    @Entity(tableName = "character")
    data class Character(
        @PrimaryKey val id: Long,
        val name: String,
        val description: String,
        val thumbnail: String,
        var isFavorite: Boolean = false
    ) : ResultItem()

}