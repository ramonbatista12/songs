package com.example.songs.viewModels

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

sealed class PlayerResultado(){
    object Caregando:PlayerResultado()
    object Reproduzindo:PlayerResultado()
    object Parado:PlayerResultado()


}

sealed class PlyerRepeticao(val repeticao:Int){
    object NaoRepetir:PlyerRepeticao(repeticao = 0)
    object RepetirUm:PlyerRepeticao(repeticao = 1)
    object RepetirTodos:PlyerRepeticao(repeticao = 2)
}

sealed class ListaMusicas(){
    object caregando:ListaMusicas()
    object Vasia:ListaMusicas()
    class Lista(val lista:List<MediaItem>):ListaMusicas()

}