package android.ihorkostenko.slatetestassignment.di

import android.content.Context
import android.ihorkostenko.slatetestassignment.broadcastreceivers.WifiReceiver
import android.net.wifi.WifiManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val wifiModule = module {
    single { wifireceiver(androidContext()) }
    single { wifiManager(androidApplication()) }
}

fun wifireceiver(context: Context): WifiReceiver {
    return WifiReceiver(context)
}

fun wifiManager(context: Context): WifiManager {
    return context.getSystemService(Context.WIFI_SERVICE) as WifiManager
}



