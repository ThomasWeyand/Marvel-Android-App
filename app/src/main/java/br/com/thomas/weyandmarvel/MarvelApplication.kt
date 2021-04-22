package br.com.thomas.weyandmarvel

import android.annotation.SuppressLint
import android.app.Application
import br.com.thomas.weyandmarvel.data.di.networkModule
import br.com.thomas.weyandmarvel.db.di.databaseModule
import br.com.thomas.weyandmarvel.domain.di.domainModule
import br.com.thomas.weyandmarvel.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@SuppressLint("Registered")
class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MarvelApplication)
            modules(listOf(databaseModule, networkModule, domainModule, presentationModule))
        }
    }

}