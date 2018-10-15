package android.ihorkostenko.slatetestassignment.ui.viewmodels

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.graphics.Color
import android.ihorkostenko.slatetestassignment.BR
import android.ihorkostenko.slatetestassignment.broadcastreceivers.WifiReceiver
import android.ihorkostenko.slatetestassignment.utils.Constants
import android.ihorkostenko.slatetestassignment.utils.Status
import android.ihorkostenko.slatetestassignment.utils.State
import android.net.wifi.WifiManager
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MainViewModel(
    private val geofencingClient: GeofencingClient,
    private val pendingIntent: PendingIntent,
    private val wifiReceiver: WifiReceiver,
    private val wifiManager: WifiManager
) :
    ViewModel(), Observable {
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    private lateinit var googleMap: GoogleMap
    private lateinit var latLng: LatLng

    @Bindable
    var radius: Float = Constants.MIN_RADIUS
    @Bindable
    var status: Status = Status.UNKNOWN
    @Bindable
    var state: State = State.STARTING
    @Bindable
    var currentWifiName: String = "None"

    private lateinit var startWifiName: String

    fun getGeofencingRequest(target: LatLng, radius: Float): GeofencingRequest {
        val geofence = Geofence.Builder()
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
            .setCircularRegion(target.latitude, target.longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setRequestId("Geofence")
            .build()
        return GeofencingRequest.Builder()
            .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_EXIT or Geofence.GEOFENCE_TRANSITION_ENTER)
            .addGeofence(geofence)
            .build()
    }

    @SuppressLint("MissingPermission")
    fun addGeofencing(target: LatLng, radius: Float) {
        geofencingClient.addGeofences(getGeofencingRequest(target, radius), pendingIntent)
    }

    fun removeGeofencing() {
        geofencingClient.removeGeofences(pendingIntent)
    }

    fun onAddGeofenceClick(view: View) {
        addMarker(latLng)
        wifiReceiver.start()
        addGeofencing(this.googleMap.cameraPosition.target, radius)
        startWifiName = wifiManager.connectionInfo.ssid
        state = State.WORKING
        notifyPropertyChanged(BR.state)
    }

    fun onRemoveGeofenceClick(view: View) {
        googleMap.clear()
        wifiReceiver.stop()
        removeGeofencing()
        state = State.WAITING
        notifyPropertyChanged(BR.state)
    }

    fun onProgressChanged(progress: Int) {
        radius = Constants.MIN_RADIUS + progress
        notifyPropertyChanged(BR.radius)
    }

    private fun addMarker(latLng: LatLng) {
        googleMap.clear()

        googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(radius.toDouble())
                .strokeColor(Color.RED)
                .fillColor(Color.GRAY)
        )
        googleMap.addMarker(MarkerOptions().position(latLng))
    }

    fun onGeofenceStatusChanged(status: Int) {
        if (this.status != Status.UNKNOWN) {
            if (startWifiName == wifiManager.connectionInfo.ssid) {
                return
            }
        }

        Status.from(status)?.let {
            this.status = it
            notifyPropertyChanged(BR.status)
        }
    }

    fun onWifiStatusChanged() {
        currentWifiName = wifiManager.connectionInfo.ssid
        notifyPropertyChanged(BR.currentWifiName)
    }

    @SuppressLint("MissingPermission")
    fun omMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.setOnCameraIdleListener {
            latLng = googleMap.cameraPosition.target
            state = State.WAITING
            notifyPropertyChanged(BR.state)
        }
        this.googleMap.isMyLocationEnabled = true
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

}
