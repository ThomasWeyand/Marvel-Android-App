package br.com.thomas.weyandmarvel.db.di

import android.app.Application
import androidx.room.Room
import br.com.thomas.weyandmarvel.db.CharacterDAO
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single { provideDatabase(androidApplication()) }
    single { provideCharacterDAO(get()) }

}

fun provideDatabase(application: Application): CharacterDatabase {
    return Room.databaseBuilder(application, CharacterDatabase::class.java, "Heroes.db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideCharacterDAO(database: CharacterDatabase): CharacterDAO {
    return  database.characterDao()
}