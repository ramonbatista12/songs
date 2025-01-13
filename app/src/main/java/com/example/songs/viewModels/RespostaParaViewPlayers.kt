package com.example.songs.viewModels

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

sealed class PlayerResultado(){
    object Caregando:PlayerResultado()
    object Reproduzindo:PlayerResultado()
    object Parado:PlayerResultado()


}



sealed class ListaMusicas(){
    object caregando:ListaMusicas()
    object Vasia:ListaMusicas()
    class Lista(val lista:List<MediaItem>):ListaMusicas()

}