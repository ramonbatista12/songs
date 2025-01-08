package com.example.songs.servicoDemidia

import androidx.media3.common.MediaItem

interface ComandosDePlayer {
    fun play()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun seekToIndisse(index: Int)
    fun setLista(lista: List<MediaItem>)
    fun setMediaItem(mediaItem: MediaItem)
    fun next()
    fun preview()
    fun repeticao(repeticao: Int)
    fun repeticao()
    fun aleatorio(aleatorio: Boolean)

}