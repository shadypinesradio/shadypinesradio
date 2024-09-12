/*
 * Copyright 2018 Filippo Beraldo. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shadypines.radio.player

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import com.shadypines.radio.R
import com.shadypines.radio.player.notification.DescriptionAdapter
import okio.`-DeprecatedOkio`.source

/**
 * Created by Filippo Beraldo on 29/11/2018.
 * http://github.com/beraldofilippo
 *
 * Just a simple injection object, builds stuff.
 */
object PlayerModule {

    private var title:String? = ""
    private var description:String? = ""
    private var bitmap:Bitmap? = null

    fun getPlayerHolder(context: Context) = PlayerHolder(context, PlayerState())


    fun getPlayerNotificationManager(exoPlayer: ExoPlayer, notificationListener: PlayerNotificationManager.NotificationListener, context: Context, title: String, desc: String, bitmap: Bitmap):PlayerNotificationManager {
        val myNoti = PlayerNotificationManager.Builder(
            context, NewPlayService.NOTIFICATION_ID, NewPlayService.NOTIFICATION_CHANNEL
        ).setMediaDescriptionAdapter(getDescriptionAdapter(context,title,desc,bitmap))
        myNoti.setNotificationListener(notificationListener)
        myNoti.setChannelImportance(NotificationUtil.IMPORTANCE_LOW)
        myNoti.setChannelNameResourceId(R.string.app_name)


        val notiBuilder = myNoti.build()
        notiBuilder.setUseNextAction(false)
        notiBuilder.setUsePreviousAction(false)
        notiBuilder.setUseStopAction(false)
        notiBuilder.setUsePlayPauseActions(true)
        notiBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        notiBuilder.setPlayer(exoPlayer)
        return notiBuilder
    }


    private fun getDescriptionAdapter(context: Context, title: String, subtitle: String, bitmap: Bitmap) =
            DescriptionAdapter(context,title, subtitle, bitmap)

}
