package com.mayank.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mayank.myapplication.mayank.MainActivity

class MyMessagingService : FirebaseMessagingService(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(p0: RemoteMessage) {
        val title : String? = p0.notification?.title
        val body  : String? = p0.notification?.body
        shownotification(title,body)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun shownotification(title : String?, text : String?)
    {
      val intent = Intent(this,MainActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        val cid = "fcm_default_channel"
        val dsounduri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val nbuilder = NotificationCompat.Builder(this,cid)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setSound(dsounduri)
            .setPriority(Notification.BADGE_ICON_LARGE)
        val rManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val oChannel = NotificationChannel(cid,"Customer",NotificationManager.IMPORTANCE_HIGH)
        rManager.createNotificationChannel(oChannel)
        rManager.notify(0,nbuilder.build())

    }
}