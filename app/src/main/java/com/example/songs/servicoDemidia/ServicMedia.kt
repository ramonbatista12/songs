package com.example.songs.servicoDemidia

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
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
    val serviceIniciado= MutableStateFlow(false)
    var _serviceIniciado=serviceIniciado.asStateFlow()
    val binder=ServicBinder()
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return this.mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        super.onCreate()
        Log.i("service","onCreate")
        scope.launch {
        val player = ExoPlayer.Builder(this@ServicMedia).build()

        mediaSession = MediaSession.Builder(this@ServicMedia, player).build()
       helperPalyer = HelperPalyerEstados(mediaSession!!)
       helperPalyerComandes = HelperPalyerComandes(mediaSession!!)

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




    override fun onDestroy() {
        Log.i("service","onDestroy")
        mediaSession.apply {
            this!!.player.release()
          }
        mediaSession=null
        helperPalyer=null
        helperPalyerComandes=null
        job.cancel()

        super.onDestroy()
    }
    interface Mybinder{
        fun getService(): ServicMedia
    }

    }

