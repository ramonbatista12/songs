package com.songsSongs.songs.servicoDemidia

import android.annotation.SuppressLint
import android.app.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.AudioAttributes
import androidx.media3.common.AuxEffectInfo
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.BitmapLoader
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.getMetaData2
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import com.songsSongs.songs.servicoDemidia.Equalizacao.Equalizador
import com.songsSongs.songs.servicoDemidia.Equalizacao.PrioridadesDaEqualizacao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ServicMedia: MediaSessionService() {

    private val job= Job()
    private val scope= CoroutineScope(Dispatchers.Main+job)
    private var mediaSession: MediaSession? = null
    var helperPalyer: HelperPalyerEstados? = null
    var helperPalyerComandes: HelperPalyerComandes? = null
    var helperNotificacao: HelperNotification? = null
    val plyListStados= MutableStateFlow<PlyListStados>(PlyListStados.Todas)
    private lateinit var notification: Notification
    private var exeuctor:ListeningExecutorService? = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor())
    private var wakeLock:PowerManager.WakeLock?=null
    val binder=ServicBinder()
    var equalizador: Equalizador?=null

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.i("service","onGetSession")
        return this.mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
         super.onStartCommand(intent, flags, startId)

        return START_NOT_STICKY
    }

    @OptIn(UnstableApi::class)
    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()
        Log.i("service","onCreate")
        scope.launch {
        criarNotificacao()
        startForeground(1,notification,ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        val player = iniciarExoplyer()
        iniciarEqualisacao(player)
        mediaSession = iniciarMeidiaSessioan(player)
        iniciarAlsiliares()
        obterWakeLoker()    }
    }


    private suspend fun iniciarExoplyer ():ExoPlayer{
      val player=  ExoPlayer.Builder(this@ServicMedia)
       .setAudioAttributes(AudioAttributes.DEFAULT,true)
       .build()
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.addAnalyticsListener(EventLogger())
        player.setHandleAudioBecomingNoisy(true)
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
        return player
    }

    @OptIn(UnstableApi::class)
    private suspend fun iniciarMeidiaSessioan(player: ExoPlayer)=MediaSession.Builder(this@ServicMedia, player)
        .setBitmapLoader(object: BitmapLoader{
            override fun supportsMimeType(mimeType: String): Boolean {
                return true
            }

            override fun decodeBitmap(data: ByteArray): ListenableFuture<Bitmap> {

                val future=exeuctor?.submit(Callable{
                    BitmapFactory.decodeByteArray(data,0,data.size)
                })
                return future!!

            }

            override fun loadBitmap(uri: Uri): ListenableFuture<Bitmap> {
                val future:ListenableFuture<Bitmap> = exeuctor!!.submit ( Callable{
                    val arrayStrings = uri.toString().split("/")
                    val id = arrayStrings[arrayStrings.size-1].split(".")[0]
                    val bitmap = getMetaData2(uri,id.toLong(),this@ServicMedia,100,100)
                    bitmap ?: this@ServicMedia.getDrawable(R.drawable.inomeado)?.toBitmap(100,100,null)
                })
                return future
            }
        })
        .build()

    private suspend fun iniciarAlsiliares(){
        helperPalyer = HelperPalyerEstados(mediaSession!!)
        helperPalyerComandes = HelperPalyerComandes(mediaSession!!)
        helperNotificacao=HelperNotification(notification=notification,
            helperPalyerEstados = helperPalyer!!,
            helperPalyerComandes = helperPalyerComandes!!,
            seviceContext = this@ServicMedia,
            secaoDeMedia = mediaSession!!)
    }

    private suspend fun obterWakeLoker(){
        val powerManager=getSystemService(POWER_SERVICE) as PowerManager
        wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"servico:lock")
        wakeLock?.acquire()
    }

    @OptIn(UnstableApi::class)
    private suspend fun iniciarEqualisacao(player: ExoPlayer){
        equalizador= Equalizador(PrioridadesDaEqualizacao.Alta,player.audioSessionId,this@ServicMedia)
        player.setAuxEffectInfo(AuxEffectInfo(equalizador!!.equalizer.id,1f))
        equalizador!!.ativar()
    }



    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return binder}

    inner  class ServicBinder:Binder(){
         fun getService(): ServicMedia {
            return this@ServicMedia
        }

    }



@RequiresApi(Build.VERSION_CODES.O)
private fun criarNotificacao(){
    val canal =NotificationChannel(ObjetoDadosDeNotificao.idCanal,ObjetoDadosDeNotificao.nomeCanal,NotificationManager.IMPORTANCE_HIGH).apply {
        description=ObjetoDadosDeNotificao.descricaoCanal
    }
    val notificationManager=(getSystemService(NotificationManager::class.java) as NotificationManager).createNotificationChannel(canal)
    notification=NotificationCompat.Builder(this,"1").setContentTitle("serviço de media")
                                                                    .setContentText("rodando")
                                                                     .setSmallIcon(R.drawable.baseline_music_note_24_darkpink).build()
}
    fun muudarPlyList(plyListStado: PlyListStados){
        scope.launch {

           plyListStados.emit(plyListStado)

        }
    }
 private fun camcelarNotificacao(){
     val notificationManager=getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(ObjetoDadosDeNotificao.idNotificao)
 }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        Log.i("service","onDestroy")
        if(mediaSession!=null)
        mediaSession.apply {

            this!!.player.release()
            this!!.release()
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

        camcelarNotificacao()
     if (equalizador!=null)
           equalizador!!.finalizar()

     if(exeuctor!=null)
          exeuctor?.shutdown()
     exeuctor=null
        job.cancel()
     if (wakeLock!=null) {
         wakeLock?.release()
     }
     wakeLock=null

        super.onDestroy()
    }


    }


