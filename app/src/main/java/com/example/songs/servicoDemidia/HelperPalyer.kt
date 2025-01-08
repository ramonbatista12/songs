package com.example.songs.servicoDemidia

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/*
* classe HelperPalyerEstados
* classe responsavel por gerenciar os estados do player
* como  loding reproducao
* os estados sao emitidos por flow e coletados por collect para gerenciar os
* estados do player que podem ser pasados para a tela principal ou para o view model
*
* */


class HelperPalyerEstados(val mediaSession: MediaSession): AuxilarMediaSecion {
    private val job = Job()
    private val scopoCorotina = CoroutineScope(Dispatchers.Main + job)
    private val tempoDereproducao = MutableStateFlow(0L)
    private val tempoTotal = MutableStateFlow(0L)
    private val estaReproduzindo = MutableStateFlow(false)
    private val loding = MutableStateFlow(false)
    private val caregando = MutableStateFlow(false)
    private val metadataAtual= MutableStateFlow<MediaItem?>(null)
    private val emplyer = MutableStateFlow(false)
    private val repeticao = MutableStateFlow(0)
    private val aleatorio = MutableStateFlow(false)
    var _loding = loding.asStateFlow()
    var _repeticao = repeticao.asStateFlow()
    var _aleatorio = aleatorio.asStateFlow()
    var _tempoTotal = tempoTotal.asStateFlow()
    var _tempoDereproducao = tempoDereproducao.asStateFlow()
    var _estaReproduzindo = estaReproduzindo.asStateFlow()
    val caregando_ = caregando.asStateFlow()
    var _metadataAtual = metadataAtual.asStateFlow()
    init {
        scopoCorotina.launch {
            fluxoTempoDereproducao().collect {
                tempoDereproducao.value = it

            }
        }
        scopoCorotina.launch {
            fluxoTempoTotal().collect {
                tempoTotal.value = it
            }
        }
        scopoCorotina.launch {
            fluxoEmplyer().collect {
                estaReproduzindo.value = it
            }
        }
        scopoCorotina.launch {
            fluxoLoding().collect {
                loding.value = it
            }
        }
        scopoCorotina.launch {
            metaDataAtual().collect{
                metadataAtual.value=it
            }

        }
        scopoCorotina.launch {
            fluxoRepeticao().collect{
                repeticao.value=it
            }
        }
        scopoCorotina.launch {
            fluxoAleatorio().collect{
                aleatorio.value=it
            }
        }
    }



private fun fluxoAleatorio() =flow<Boolean> {
    while (true){
      emit(mediaSession.player.shuffleModeEnabled)
        delay(1000)
    }
}

private fun fluxoRepeticao()= flow<Int> {
    while (true){
        emit(mediaSession.player.repeatMode)
        delay(1000)
    }
}

private fun fluxoTempoDereproducao()= flow<Long> {

     while (true) {

         emit(mediaSession.player.currentPosition)
         Log.d("duracao do conteudo",mediaSession.player.currentPosition.toString())
         delay(1000)
     }
 }

 private fun fluxoTempoTotal()= flow<Long> {
     while (true){

         emit(mediaSession.player.duration)
         Log.d("duracao do conteudo","duracao total:"+mediaSession.player.duration.toString())
         delay(1000)
     }
 }

 private fun fluxoEmplyer()= flow<Boolean> {
     while (true){

         emit(mediaSession.player.isPlaying)
         delay(1000)
     }
 }

private fun fluxoLoding()= flow<Boolean> {
    while (true){
        emit(mediaSession.player.isLoading)
        delay(1000)
    }
}
private fun metaDataAtual()= flow<MediaItem?> {
    while(true){
   emit(mediaSession.player.currentMediaItem)
    delay(1000)}
}
   override fun finalizar(){
        job.cancel()

    }

}


class HelperPalyerComandes(val mediaSession: MediaSession):ComandosDePlayer,AuxilarMediaSecion{
    private val job= Job()
    private val scopoCorotina= CoroutineScope(Dispatchers.Main+job)

    override fun play() {
         Log.d("controle plyer","ply chamdo no service")
        scopoCorotina.launch {
            Log.d("controle plyer","ply chamdo no service dentro da corotina")
            mediaSession.player.prepare()
            mediaSession.player.play()
        }
    }

    override fun pause() {
        scopoCorotina.launch {
            mediaSession.player.pause()
        }
    }

    override fun stop() {
        mediaSession.player.stop()
    }

    override fun seekTo(position: Long) {
       scopoCorotina.launch {
           mediaSession.player.seekTo(position)
       }
    }

    override fun seekToIndisse(index: Int) {
        scopoCorotina.launch {
            mediaSession.player.seekTo(index,0L)
        }
    }

    override fun setLista(lista: List<MediaItem>) {
       scopoCorotina.launch {
           mediaSession.player.setMediaItems(lista)
       }
    }

    fun setLista(lista: List<MediaItem>,index:Int){
        scopoCorotina.launch {
            mediaSession.player.setMediaItems(lista,index,0L)

        }
    }

    override fun setMediaItem(mediaItem: MediaItem) {
       scopoCorotina.launch {

           mediaSession.player.setMediaItem(mediaItem)
       }
    }

    override fun next() {4
        scopoCorotina.launch {
            mediaSession.player.seekToNext()
        }

    }

    override fun preview() {
        scopoCorotina.launch {
            mediaSession.player.seekToPrevious()
        }

    }

    override fun repeticao(repeticao: Int) {
        scopoCorotina.launch {
            mediaSession.player.repeatMode=repeticao
        }
    }

    override fun repeticao() {

    }

    override fun aleatorio(aleatorio: Boolean) {
       scopoCorotina.launch {
           mediaSession.player.shuffleModeEnabled=aleatorio
       }
    }



    override fun finalizar() {
        job.cancel()
    }

}

interface AuxilarMediaSecion{
    fun finalizar()
}

