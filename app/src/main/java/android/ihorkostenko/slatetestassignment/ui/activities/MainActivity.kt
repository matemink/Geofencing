package android.ihorkostenko.slatetestassignment.ui.activities

import android.Manifest
import android.ihorkostenko.slatetestassignment.R
import android.ihorkostenko.slatetestassignment.databinding.MainActivityBinding
import android.ihorkostenko.slatetestassignment.events.GeofenceEvent
import android.ihorkostenko.slatetestassignment.events.WifiEvent
import android.ihorkostenko.slatetestassignment.ui.viewmodels.MainViewModel
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.main_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel
import permissions.dispatcher.*


@RuntimePermissions
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.main_activity)

        val binding: MainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.viewModel = mainViewModel

        if (savedInstanceState == null) {
            showMapFragmentWithPermissionCheck()
        }
        initSeekBar()
    }

    private fun initSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mainViewModel.onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGeofenceEvent(event: GeofenceEvent) {
        mainViewModel.onGeofenceStatusChanged(event.status)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWifiEvent(event: WifiEvent) {
        mainViewModel.onWifiStatusChanged()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mainViewModel.omMapReady(googleMap)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showMapFragment() {
        val mapFragment = SupportMapFragment.newInstance()
        mapFragment.getMapAsync(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.map, mapFragment)
            .commitNow()
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showRationaleForGps(request: PermissionRequest) {
        //showRationaleDialog(R.string.permission_camera_rationale, request)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onGpsDenied() {
        //Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onGpsNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_camera_never_askagain, Toast.LENGTH_SHORT).show()
    }

}
