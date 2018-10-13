package android.ihorkostenko.slatetestassignment.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.ihorkostenko.slatetestassignment.events.WifiEvent
import android.net.wifi.WifiManager
import org.greenrobot.eventbus.EventBus

class WifiReceiver(private var context: Context) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            EventBus.getDefault().post(WifiEvent())
        }
    }

    fun start() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        context.registerReceiver(this, intentFilter)
    }

    fun stop() {
        try {
            context.unregisterReceiver(this)
        } catch (e: IllegalArgumentException) {
        }
    }
}
