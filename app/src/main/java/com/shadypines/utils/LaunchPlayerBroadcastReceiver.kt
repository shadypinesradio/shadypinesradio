package com.shadypines.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shadypines.radio.view.activity.TempActivity

class LaunchPlayerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifyIntent = Intent(context, TempActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(notifyIntent)
    }
}