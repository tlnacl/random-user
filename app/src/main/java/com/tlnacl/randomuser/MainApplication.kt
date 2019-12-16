package com.tlnacl.randomuser

import android.app.Application
import com.tlnacl.randomuser.di.AppComponent
import com.tlnacl.randomuser.di.AppModule
import com.tlnacl.randomuser.di.DaggerAppComponent
import timber.log.Timber

class MainApplication : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}