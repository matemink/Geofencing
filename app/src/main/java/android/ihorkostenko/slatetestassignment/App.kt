package android.ihorkostenko.slatetestassignment

import android.app.Application
import android.ihorkostenko.slatetestassignment.di.geofencingModule
import android.ihorkostenko.slatetestassignment.di.mainModule
import android.ihorkostenko.slatetestassignment.di.wifiModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin(this, listOf(geofencingModule, wifiModule, mainModule))
    }
}