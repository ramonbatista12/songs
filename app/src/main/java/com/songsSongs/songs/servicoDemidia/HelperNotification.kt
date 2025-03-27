package com.songsSongs.songs.servicoDemidia

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.songsSongs.songs.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
* classes usadas para gerenciar notificacoes do servico de media
* por nao saber o porque de mediassesison service nao criar de forma altomatica as notificacoes de media
* opitei por eu mesmo criar minhas proprias notificacoes foi utilizado corrotinas e colet de fluxo para observar
* mudancas no plyer como mudanca de metaData e mudanca de estado de reproducao para lansar as Notificacoes
* */
@RequiresApi(Build.VERSION_CODES.O)
class HelperNotification(val notification: Notification,
                         val helperPalyerEstados: HelperPalyerEstados,
                         val helperPalyerComandes: HelperPalyerComandes,
                         val seviceContext: Context,
                         val secaoDeMedia:MediaSession):AuxilarMediaSecion
{
    val job= Job()
    val scope= CoroutineScope(Dispatchers.Main+job)
    val metaData= MutableStateFlow<MediaItem?>(null)
    val fabricaDeNotificacoes= FabricaDeNotificacoes(notification,seviceContext,secaoDeMedia)

    init {
        Log.i("service","helper notificacao")







    scope.launch(Dispatchers.Default) {
        scope.launch(Dispatchers.Default) {
        Log.i("service","helper notificacao scope")
        helperPalyerEstados._metadataAtual.collect{
            metaData.value=it
            if(it!=null)
            fabricaDeNotificacoes.atualizarNotificacao(true,metaData.value)
        }}
      scope.launch(Dispatchers.Default) {
          helperPalyerEstados._estaReproduzindo.collect{
              Log.i("service","helper notificacao scope esta reproduzindo $it")
               fabricaDeNotificacoes.atualizarNotificacao(it,metaData.value)


          }
           }



    }



    }
    override fun finalizar() {
        job.cancel()
        metaData.value=null


        

    }

}

/*
* o receiver e  e um broadcast receiver que eu o registro programaticamente
* ele recebe um ponteiro que e passado por referencia pela jvm
* o ponteiro aponta para a classe principal do gerenciamento de notificaoes o
* HelperNotification e funciona como um listener para responder as peddings intentes de cada
* acao nas notificacoes
* */

/*
* a fabrica de notificacoes tem os metodos nesssesarios para atualizar as notificacoes
* as notificacoes sao pasadas como referencias e assesadas como um ponteiro
*
* */
@RequiresApi(Build.VERSION_CODES.O)
class FabricaDeNotificacoes(var notification: Notification, val contextoDoServico: Context,val secaoDemedia: MediaSession){

    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.P)
    fun atualizarNotificacao(reprodusindo:Boolean, metaData:MediaItem?){
        if(reprodusindo){
            val dadosTitulo=metaData?.mediaMetadata?.title ?: "sem titulo"
            val a=MediaStyleNotificationHelper.MediaStyle(secaoDemedia).build()
            notification=NotificationCompat.Builder(contextoDoServico,"1").setSmallIcon(R.drawable.inomeado)
                .setContentText("Reproduzindo ${ if(metaData!=null)dadosTitulo else "sem titulo"}  ")
                .setContentTitle(dadosTitulo)//.createWithResource()
                .setSilent(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setStyle(MediaStyleNotificationHelper.MediaStyle(secaoDemedia))
                .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)



        }

        else{
            notification=NotificationCompat.Builder(contextoDoServico,"1")
                                                                             .setSmallIcon(R.drawable.inomeado)
                                                                             .setContentText("Pronto para reprodusir")
                                                                             .setContentTitle("servico de media")
                                                                             .setStyle(NotificationCompat.BigPictureStyle())
                                                                             .setPriority(NotificationCompat.PRIORITY_MIN)
                                                                             .setSilent(true)
                                                                             .build()

            NotificationManagerCompat.from(contextoDoServico).notify(1,notification)
        }
    }

    fun criarPeddingIntent(acao:String):PendingIntent{
        val intent=Intent(acao)

        return PendingIntent.getBroadcast(contextoDoServico,1,intent,PendingIntent.FLAG_IMMUTABLE)
    }



}

