package com.example.songs.servicoDemidia

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HelperPalyerEstados(val mediaSession: MediaSession): AuxilarMediaSecion {
    private val job= Job()
    private val scopoCorotina= CoroutineScope(Dispatchers.Main+job)
    private val tempoDereproducao=MutableStateFlow(0L)
    private val tempoTotal=MutableStateFlow(0L)
    private val estaReproduzindo=MutableStateFlow(false)
    private val lista = MutableStateFlow<List<MediaItem>>(emptyList())
    var _tempoTotal=tempoTotal.asStateFlow()
    var _tempoDereproducao=tempoDereproducao.asStateFlow()
    var _estaReproduzindo=estaReproduzindo.asStateFlow()

    init {

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