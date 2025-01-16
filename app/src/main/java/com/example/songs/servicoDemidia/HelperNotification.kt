package com.example.songs.servicoDemidia

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.collection.emptyLongSet
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.IntentCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.session.MediaStyleNotificationHelper.MediaStyle
import com.example.songs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@RequiresApi(Build.VERSION_CODES.O)
class HelperNotification(val notification: Notification,
                         val helperPalyerEstados: HelperPalyerEstados,
                         val helperPalyerComandes: HelperPalyerComandes,
                         val seviceContext: Context,
                         val secaoDeMedia:MediaSession):AuxilarMediaSecion{
    val job= Job()
    val scope= CoroutineScope(Dispatchers.Main+job)
    val metaData= MutableStateFlow<MediaItem?>(null)
    val fabricaDeNotificacoes= FabricaDeNotificacoes(notification,seviceContext)

    private val receiver=Receiver(this)
    init {
        Log.i("service","helper notificacao")
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
        val registerReceiver=seviceContext.registerReceiver(receiver, IntentFilter().apply {
            addAction(MensagemsBroadcast.play.mensagem)
            addAction(MensagemsBroadcast.pause.mensagem)
            addAction(MensagemsBroadcast.next.mensagem)
            addAction(MensagemsBroadcast.preview.mensagem)

        },Context.RECEIVER_NOT_EXPORTED)}
        else{
        val registerReceiver=seviceContext.registerReceiver(receiver, IntentFilter().apply {
            addAction(MensagemsBroadcast.play.mensagem)
            addAction(MensagemsBroadcast.pause.mensagem)
            addAction(MensagemsBroadcast.next.mensagem)
            addAction(MensagemsBroadcast.preview.mensagem)

        })
        }
    scope.launch {
        scope.launch {
        Log.i("service","helper notificacao scope")
        helperPalyerEstados._metadataAtual.collect{
            metaData.value=it
            if(it!=null)
            fabricaDeNotificacoes.atualizarNotificacao(true,metaData.value)
        }}
      scope.launch {
          helperPalyerEstados._estaReproduzindo.collect{
              Log.i("service","helper notificacao scope esta reproduzindo $it")
              if(!it) fabricaDeNotificacoes.atualizarNotificacao(false,metaData.value)
              else fabricaDeNotificacoes.atualizarNotificacao(true,metaData.value)

          }
           }
        scope.launch {
            helperPalyerEstados._tempoDereproducao.map {
            val f:Float=    (it*100f)/helperPalyerEstados._tempoTotal.value
                f
            }.collect{
                if(helperPalyerEstados._estaReproduzindo.value&&metaData.value!=null)
                    fabricaDeNotificacoes.atualizarNotificacaoComprogresso(true,metaData.value,it,secaoDeMedia)
            }
        }


    }



    }
    override fun finalizar() {
        job.cancel()
        metaData.value=null
        

    }
    fun play(){
        helperPalyerComandes.prepare()
        helperPalyerComandes.play()
    }
    fun pause(){
        helperPalyerComandes.pause()
    }
    fun next(){
        helperPalyerComandes.next()
    }
    fun preview(){
        helperPalyerComandes.preview()
    }
    fun stop(){
        helperPalyerComandes.stop()
    }
}


class Receiver(val HelperNotification: HelperNotification): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      if(intent!=null){
          when(intent.action){
              MensagemsBroadcast.play.mensagem->{
                  HelperNotification.play()
              }
              MensagemsBroadcast.pause.mensagem->{
                  HelperNotification.pause()
              }
              MensagemsBroadcast.next.mensagem->{
                  HelperNotification.stop()
                  HelperNotification.next()
                  HelperNotification.play()

              }
              MensagemsBroadcast.preview.mensagem->{
                  HelperNotification.preview()
              }
              MensagemsBroadcast.stop.mensagem->{
                  HelperNotification.stop()
              }
              else->{}
          }
      }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
class FabricaDeNotificacoes(var notification: Notification, val contextoDoServico: Context){

    @RequiresApi(Build.VERSION_CODES.P)
    fun atualizarNotificacao(reprodusindo:Boolean, metaData:MediaItem?){
        if(reprodusindo){
            val dadosTitulo=metaData!!.mediaMetadata.title

            notification=Notification.Builder(contextoDoServico,"1").setSmallIcon(R.drawable.baseline_music_note_24_darkpink)
                .setContentText("Reproduzindo $dadosTitulo ")
                .setContentTitle(dadosTitulo)//.createWithResource()

               .addAction(android.app.Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_skip_previous_24
                ) ,"voltar",criarPeddingIntent(MensagemsBroadcast.preview.mensagem)).build())

                .addAction(Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_pause_24
                ),"parar",criarPeddingIntent(MensagemsBroadcast.pause.mensagem)).build())

                .addAction(Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_skip_next_24
                ),"proxima",criarPeddingIntent(MensagemsBroadcast.next.mensagem)).build())
                .setStyle(Notification.MediaStyle())
                .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)



        }

        else{
            notification=Notification.Builder(contextoDoServico,"1").setSmallIcon(R.drawable.baseline_music_note_24_darkpink)
                                                                .setContentText("Pronto para reprodusir")
                                                                .setContentTitle("servico de media")
                                                                .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)
        }
    }

    fun criarPeddingIntent(acao:String):PendingIntent{
        val intent=Intent(acao)

        return PendingIntent.getBroadcast(contextoDoServico,1,intent,PendingIntent.FLAG_IMMUTABLE)
    }

    fun atualizarNotificacaoComprogresso(reprodusindo:Boolean, metaData:MediaItem?,progresso:Float,secaoDemedia:MediaSession){
        if(reprodusindo){
            val dadosTitulo=metaData!!.mediaMetadata.title
            val progresso=progresso.toInt()
            notification=Notification.Builder(contextoDoServico,"1").setSmallIcon(R.drawable.baseline_music_note_24_darkpink)
                .setContentText("Reproduzindo $dadosTitulo ")
                .setContentTitle(dadosTitulo)//.createWithResource()

                .addAction(android.app.Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_skip_previous_24
                ) ,"voltar",criarPeddingIntent(MensagemsBroadcast.preview.mensagem)).build())
                .addAction(Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_pause_24
                ),"parar",criarPeddingIntent(MensagemsBroadcast.pause.mensagem)).build())

                .addAction(Notification.Action.Builder(Icon.createWithResource(contextoDoServico,
                    R.drawable.baseline_skip_next_24
                ),"proxima",criarPeddingIntent(MensagemsBroadcast.next.mensagem)).build())
                .setProgress(100,progresso,false)
                .setStyle(Notification.MediaStyle())

                .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)



        }

        else{
            notification=Notification.Builder(contextoDoServico,"1").setSmallIcon(R.drawable.baseline_music_note_24_darkpink)
                .setContentText("Pronto para reprodusir")
                .setContentTitle("servico de media")

                .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)
        }
    }
}

sealed class MensagemsBroadcast(val mensagem:String){
    object play: MensagemsBroadcast("play")
    object pause: MensagemsBroadcast("pause")
    object next: MensagemsBroadcast("next")
    object preview: MensagemsBroadcast("preview")
    object stop: MensagemsBroadcast("stop")

}
