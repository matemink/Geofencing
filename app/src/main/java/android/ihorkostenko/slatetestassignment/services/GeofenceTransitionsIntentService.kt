package android.ihorkostenko.slatetestassignment.services

import android.app.IntentService
import android.content.Intent
import android.ihorkostenko.slatetestassignment.events.GeofenceEvent
import com.google.android.gms.location.GeofencingEvent
import org.greenrobot.eventbus.EventBus


class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            return
        }
        EventBus.getDefault().post(GeofenceEvent(geofencingEvent.geofenceTransition))
    }
}
