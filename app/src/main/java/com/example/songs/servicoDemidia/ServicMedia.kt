package com.example.songs.servicoDemidia

import android.annotation.SuppressLint
import android.app.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.os.PowerManager
import android.util.EventLog
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.media3.common.AudioAttributes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.BitmapLoader
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.songs.R
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.FileDescriptor

class ServicMedia: MediaSessionService() {

    val job= Job()
    val scope= CoroutineScope(Dispatchers.Main+job)
    var mediaSession: MediaSession? = null
    var helperPalyer: HelperPalyerEstados? = null
    var helperPalyerComandes: HelperPalyerComandes? = null
    var helperNotificacao: HelperNotification? = null
    val serviceIniciado= MutableStateFlow(false)
    var _serviceIniciado=serviceIniciado.asStateFlow()
    val plyListStados= MutableStateFlow<PlyListStados>(PlyListStados.Todas)
    lateinit var notification: Notification
    val binder=ServicBinder()
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.i("service","onGetSession")
        return this.mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         super.onStartCommand(intent, flags, startId)

        return START_NOT_STICKY
    }
    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()
        Log.i("service","onCreate")
        scope.launch {
        criarNotificacao()

        startForeground(1,notification,ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        val player = ExoPlayer.Builder(this@ServicMedia).setAudioAttributes(AudioAttributes.DEFAULT,true)
                                                                .build()
        player.addAnalyticsListener(EventLogger())
        player.addListener(object :Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                player.seekToNext()
                player.prepare()
                player.play()
                var nome =if(helperPalyer!=null)   helperPalyer?._metadataAtual?.value?.mediaMetadata?.title?:"" else ""
                Toast.makeText(this@ServicMedia,"Erro ao reprodusir  a faixa $nome",Toast.LENGTH_SHORT).show()
            }
        })


       mediaSession = MediaSession.Builder(this@ServicMedia, player).build()
       //onUpdateNotification(mediaSession!!,true)
       helperPalyer = HelperPalyerEstados(mediaSession!!)
       helperPalyerComandes = HelperPalyerComandes(mediaSession!!)
       helperNotificacao=HelperNotification(notification=notification,
           helperPalyerEstados = helperPalyer!!,
                                            helperPalyerComandes = helperPalyerComandes!!,
                                            seviceContext = this@ServicMedia,
                                            secaoDeMedia = mediaSession!!)
       val powerManager=getSystemService(POWER_SERVICE) as PowerManager
       val wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TAG")
       wakeLock.acquire()
            serviceIniciado.emit(true)
        }


    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)

        return binder
    }
    inner  class ServicBinder:Binder(){
         fun getService(): ServicMedia {
            return this@ServicMedia
        }

    }



@RequiresApi(Build.VERSION_CODES.O)
private fun criarNotificacao(){
    val canal =NotificationChannel("1","serviceMedia",NotificationManager.IMPORTANCE_HIGH).apply {
        description="notificacao do servico de media"
    }
    val notificationManager=(getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(canal)
    notification=Notification.Builder(this,"1").setContentTitle("servico de media")
                                                                    .setContentText("rodando")
                                                                     .setSmallIcon(R.drawable.baseline_music_note_24_darkpink).build()
}
    fun muudarPlyList(plyListStado: PlyListStados){
        scope.launch {

           plyListStados.emit(plyListStado)
            Log.d("service","muudarPlyList: $plyListStado")
        }
    }
    override fun onDestroy() {
        Log.i("service","onDestroy")
        if(mediaSession!=null)
        mediaSession.apply {
            this!!.player.release()
          }
        mediaSession=null
        if(helperPalyer!=null){
            helperPalyer!!.finalizar()
            helperPalyer=null
        }
        if(helperPalyerComandes!=null){
            helperPalyerComandes!!.finalizar()
            helperPalyerComandes=null
        }
        if (helperNotificacao!=null){
            helperNotificacao!!.finalizar()
            helperNotificacao=null
        }

        job.cancel()

        super.onDestroy()
    }
    interface Mybinder{
        fun getService(): ServicMedia
    }

    }

