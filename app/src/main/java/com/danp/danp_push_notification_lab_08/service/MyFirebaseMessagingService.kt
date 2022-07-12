package com.danp.danp_push_notification_lab_08.service

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.danp.danp_push_notification_lab_08.MainActivity
import com.danp.danp_push_notification_lab_08.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.moengage.pushbase.ACTION_SNOOZE


class MyFirebaseMessagingService: FirebaseMessagingService() {

    fun subscribeTopics() {
        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]
    }


    companion object {
        private const val TAG = "Notificación FCM"
        const val DEFAULT_NOTIFICATION_ID = 0
        const val INTENT_REQUEST = 0
        const val BUTTON_INTENT_REQUEST = 1

    }


    override fun onNewToken(token: String) {
        Log.i(TAG, "Nuevo token FCM creado: $token")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        initNotificationChannel(notificationManager)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var notificationBuilder = if(Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            NotificationCompat.Builder(applicationContext, "1")
        } else {
            NotificationCompat.Builder(applicationContext)
        }

        //Definir intent
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("botones", true)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(INTENT_REQUEST,  PendingIntent.FLAG_UPDATE_CURRENT)
        }
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Botones de acción
        val buttonIntent1 = Intent (this, MainActivity::class.java)
        buttonIntent1.putExtra("accion", "presionado")
        val buttonPendingIntent1 = PendingIntent.getActivity(
            this,
            BUTTON_INTENT_REQUEST,
            buttonIntent1,
            PendingIntent.FLAG_ONE_SHOT
        )

        val action1 = NotificationCompat.Action.Builder(
            R.drawable.ic_lecturas,
            "Estadísticas",
            buttonPendingIntent1
        ).build()

        //Botones de acción
        val buttonIntent2 = Intent()
        buttonIntent2.setAction(Intent.ACTION_VIEW)
        buttonIntent2.setData(Uri.parse("https://revistaganamas.com.pe/tendencias-y-perspectivas-para-el-sector-educacion-en-el-2022/"))
        val buttonPendingIntent2 = PendingIntent.getActivity(
            this,
            BUTTON_INTENT_REQUEST,
            buttonIntent2,
            PendingIntent.FLAG_ONE_SHOT
        )

        val action2 = NotificationCompat.Action.Builder(
            R.drawable.ic_lecturas,
            "Referencia",
            buttonPendingIntent2
        ).build()


        //Construcción de la notificación
        notificationBuilder = notificationBuilder
            .setSmallIcon(R.drawable.ic_books)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(body))
            .setContentIntent(pendingIntent)
            .addAction(action1)
            .addAction(action2)
            .setAutoCancel(true)
        initNotificationChannel(notificationManager)
        notificationManager.notify(DEFAULT_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun initNotificationChannel(notificationManager: NotificationManager) {
        if(Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            notificationManager.createNotificationChannelIfNotExists(
                channelId = "1",
                channelName = "Default"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationManager.createNotificationChannelIfNotExists(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    var channel = this.getNotificationChannel(channelId)
    if(channel == null){
        channel = NotificationChannel(
            channelId,
            channelName,
            importance
        )
        this.createNotificationChannel(channel)
    }
}