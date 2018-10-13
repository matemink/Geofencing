package android.ihorkostenko.slatetestassignment.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.ihorkostenko.slatetestassignment.services.GeofenceTransitionsIntentService
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val geofencingModule = module {
    single { geofencingClient(androidContext()) }
    single { pendingIntent(androidApplication()) }
}

fun geofencingClient(context: Context): GeofencingClient {
    return LocationServices.getGeofencingClient(context)
}

fun pendingIntent(context: Context): PendingIntent {
    val intent = Intent(context, GeofenceTransitionsIntentService::class.java)
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

