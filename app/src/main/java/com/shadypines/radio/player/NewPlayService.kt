package com.shadypines.radio.player

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.shadypines.radio.R
import com.shadypines.radio.utils.AppSettings
import com.shadypines.radio.view.activity.TempActivity

class NewPlayService: Service(), PlayerNotificationManager.NotificationListener  {


    companion object {
        const val NOTIFICATION_ID = 101
        const val NOTIFICATION_CHANNEL = "playerlib_channel_new"
    }

    private val mIBinder = LocalBinder()

    var isRestoredFromPause = false

    public var playerHolder: PlayerHolder? = null

    public var playerNotificationManager: PlayerNotificationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your existing logic here
        return START_STICKY
    }


    override fun onDestroy() {
        playerNotificationManager?.setPlayer(null)
        stopSelf()
        stopForeground(true)
        Log.d("newPlayerTag =", "onDestroy")
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("newPlayerTag =","onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("newPlayerTag =","onBind")
        if (playerHolder==null){
            playerHolder = PlayerModule.getPlayerHolder(this.applicationContext)
        }
        return mIBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("newPlayerTag =","onUnbind")
        return super.onUnbind(intent)
    }

    inner class LocalBinder : Binder() {
        val instance: NewPlayService
            get() = this@NewPlayService
    }

    public fun setNotification() {

        try{
            var icon: Bitmap = BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_launcher_round
            )
            if (TempActivity.bitmap != null) {
                icon = TempActivity.bitmap
            }
            playerNotificationManager = PlayerModule.getPlayerNotificationManager(playerHolder?.audioFocusPlayer!!,
                this,this.applicationContext, AppSettings.title, AppSettings.album,icon)

        }catch (_: Exception){

        }

    }

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        startForeground(notificationId, notification)
        Log.d("newPlayerTag =","onNotificationPosted")
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        Log.d("newPlayerTag =","onNotificationCancelled")
    }
}