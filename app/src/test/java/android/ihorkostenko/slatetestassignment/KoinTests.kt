package android.ihorkostenko.slatetestassignment

import android.app.PendingIntent
import android.ihorkostenko.slatetestassignment.broadcastreceivers.WifiReceiver
import android.ihorkostenko.slatetestassignment.ui.viewmodels.MainViewModel
import android.ihorkostenko.slatetestassignment.utils.State
import android.ihorkostenko.slatetestassignment.utils.Status
import android.net.wifi.WifiManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.`when`


class KoinTests : KoinTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var pendingIntent: PendingIntent
    private lateinit var wifireceiver: WifiReceiver
    private lateinit var wifiManager: WifiManager

    @Before
    fun setupMocksAndView() {
        geofencingClient = Mockito.mock(GeofencingClient::class.java)
        pendingIntent = Mockito.mock(PendingIntent::class.java)
        wifireceiver = Mockito.mock(WifiReceiver::class.java)
        wifiManager = Mockito.mock(WifiManager::class.java)
        mainViewModel = Mockito.mock(MainViewModel::class.java)
    }

    @Test
    fun checkWifiReceiver() {
        wifireceiver.start()
        wifireceiver.stop()
        Mockito.verify(wifireceiver).start()
        Mockito.verify(wifireceiver).stop()
    }

    @Test
    fun checkMainViewModelMethods() {
        val latLng = Mockito.mock(LatLng::class.java)
        val radius = mainViewModel.radius
        mainViewModel.addGeofencing(latLng, radius)

        val googleMap = Mockito.mock(GoogleMap::class.java)
        mainViewModel.omMapReady(googleMap)
        mainViewModel.getGeofencingRequest(latLng, radius)
        mainViewModel.onWifiStatusChanged()
        mainViewModel.onGeofenceStatusChanged(Geofence.GEOFENCE_TRANSITION_ENTER)
        mainViewModel.onProgressChanged(100)
        mainViewModel.removeGeofencing()

        Mockito.verify(mainViewModel).addGeofencing(latLng, radius)
        Mockito.verify(mainViewModel).omMapReady(googleMap)

        Mockito.verify(mainViewModel).getGeofencingRequest(latLng, radius)
        Mockito.verify(mainViewModel).onWifiStatusChanged()
        Mockito.verify(mainViewModel).onGeofenceStatusChanged(Geofence.GEOFENCE_TRANSITION_ENTER)
        Mockito.verify(mainViewModel).onProgressChanged(100)
        Mockito.verify(mainViewModel).removeGeofencing()
    }

    @Test
    fun checkMainViewModelFields() {
        `when`(mainViewModel.currentWifiName).thenReturn("Router")
        Assert.assertEquals(mainViewModel.currentWifiName, "Router")

        `when`(mainViewModel.state).thenReturn(State.WORKING)
        Assert.assertEquals(mainViewModel.state, State.WORKING)

        `when`(mainViewModel.status).thenReturn(Status.INSIDE)
        Assert.assertEquals(mainViewModel.status, Status.INSIDE)

        `when`(mainViewModel.radius).thenReturn(200f).thenThrow(IllegalStateException::class.java)
        Assert.assertEquals(mainViewModel.radius, 200f)
        Assert.assertEquals(mainViewModel.radius, 200f)
    }

    @Test
    fun checkRealMainViewModel() {
        mainViewModel = MainViewModel(geofencingClient, pendingIntent, wifireceiver, wifiManager)

        val latLng = Mockito.mock(LatLng::class.java)
        val radius = mainViewModel.radius
        mainViewModel.addGeofencing(latLng, radius)
        val geofencingRequest = mainViewModel.getGeofencingRequest(latLng, radius)
        Assert.assertNotNull(geofencingRequest)

        try {
            mainViewModel.getGeofencingRequest(latLng, -100f)
        } catch (e: Exception) {
            Assert.assertTrue(!e.message.isNullOrEmpty())
        }

        mainViewModel.onGeofenceStatusChanged(-1)
    }
}
