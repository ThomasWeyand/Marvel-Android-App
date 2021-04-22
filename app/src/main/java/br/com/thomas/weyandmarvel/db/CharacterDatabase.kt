package br.com.thomas.weyandmarvel.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.thomas.weyandmarvel.model.ResultItem

@Database(
    entities = [ResultItem.Character::class, CharacterOffset::class, SearchCharacterOffset::class],
    version = 4,
    exportSchema = false
)
abstract class CharacterDatabase : RoomDatabase(){
    abstract fun offsetDao(): CharacterOffsetDAO
    abstract fun characterDao(): CharacterDAO
    abstract fun searchOffsetDao(): SearchCharacterOffsetDAO
}