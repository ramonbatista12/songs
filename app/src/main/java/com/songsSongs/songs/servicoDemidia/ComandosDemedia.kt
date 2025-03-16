package com.songsSongs.songs.servicoDemidia

import androidx.media3.common.MediaItem

interface ComandosDemedia{
    //basicos plyer
    fun prepare()
    fun play()
    fun pause()
    fun stop()
    //navegacao plyer
    fun seekTo(position: Long)
    fun seekToItem(position: Int)
    fun next()
    fun preview()
    //iniciar Lista / items
    fun setLista(lista: List<MediaItem>)
    fun setMediaItem(mediaItem: MediaItem)
    //
    fun setModoRepeticao(modo:Int)
    fun setModoAleatorio(shuffleModeEnabled:Boolean)


}