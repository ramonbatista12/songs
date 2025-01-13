package com.example.songs.servicoDemidia

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
    private val metadataAtual= MutableStateFlow<MediaMetadata>(MediaMetadata.Builder().build())
    private val emplyer = MutableStateFlow(false)
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

        }
    }





private fun fluxoTempoDereproducao()= flow<Long> {

     while (true) {

         emit(mediaSession.player.currentPosition)
         delay(1000)
     }
 }

 private fun fluxoTempoTotal()= flow<Long> {
     while (true){

         emit(mediaSession.player.duration)
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
private fun metaDataAtual()= flow<MediaMetadata> {
   emit(mediaSession.player.currentMediaItem!!.mediaMetadata)
    delay(1000)
}
   override fun finalizar(){
        job.cancel()

    }

}


class HelperPalyerComandes(val mediaSession: MediaSession): ComandosDemedia,AuxilarMediaSecion{
    private val job= Job()
    private val scopoCorotina= CoroutineScope(Dispatchers.Main+job)

    override fun play() {

        scopoCorotina.launch {mediaSession.player.play()}
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

    override fun setLista(lista: List<MediaItem>) {
       scopoCorotina.launch {
           mediaSession.player.setMediaItems(lista)
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

    override fun finalizar() {
        job.cancel()
    }

}

interface AuxilarMediaSecion{
    fun finalizar()
}


interface ComandosDemedia{
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun setLista(lista: List<MediaItem>)
    fun setMediaItem(mediaItem: MediaItem)
    fun next()
    fun preview()


}