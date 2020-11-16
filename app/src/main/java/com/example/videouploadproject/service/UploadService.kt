package com.example.videouploadproject.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.videouploadproject.INTENT_UPLOAD_LIST
import com.example.videouploadproject.MainActivity
import com.example.videouploadproject.R
import com.example.videouploadproject.item.VideoInfo
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.longToast

const val CHANNEL_ID = "channel"
const val CHANNEL_NAME = "upload"

class UploadService : Service() {
    lateinit var notificationManager: NotificationManager
    lateinit var notification: Notification
    var uploadVideoList: ArrayList<VideoInfo> = java.util.ArrayList()
    var currentIdx = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        intent.takeIf { it.hasExtra(INTENT_UPLOAD_LIST) }?.let {
            uploadVideoList = it.getSerializableExtra(INTENT_UPLOAD_LIST) as ArrayList<VideoInfo>
        }
        uploadVideoFile(currentIdx)
        return START_REDELIVER_INTENT
    }

    fun uploadVideoFile(index: Int) {
        Uri.parse(uploadVideoList[index].uri).let {
            val filename: String = uploadVideoList[index].title

            Log.v("seolim", "filename : " + filename)
            FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://videouploadproject-a9968.appspot.com")
                .child("video/$filename")
                .putFile(it)
                .addOnSuccessListener {
                    currentIdx++

                    if (currentIdx < uploadVideoList.size) {
                        uploadVideoFile(currentIdx)
                    } else {
                        notificationManager.cancelAll()
                        longToast("업로드 성공")
                        currentIdx = 0
                        Log.i("seolim", "Upload Success")
                    }
                }
                .addOnFailureListener {
                    longToast("업로드 실패")
                    Log.e("seolim", "Upload Fail")
                }
                .addOnProgressListener {
                    val progress: Int = ((it.bytesTransferred * 100) / it.totalByteCount).toInt()
                    showNotification(filename, progress)
                    Log.d("seolim", "progress : " + progress)
                }
        }
    }

    private fun showNotification(title: String, progress: Int) {
        val intent = Intent(baseContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this@UploadService,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(baseContext, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(baseContext)
        }

        notification = builder.setContentTitle(title)
            .setContentText("$progress%")
            .setSubText("${currentIdx + 1}/${uploadVideoList.size}")
            .setProgress(100, progress, false)
            .setSmallIcon(R.drawable.file_upload)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(123, notification)
    }
}