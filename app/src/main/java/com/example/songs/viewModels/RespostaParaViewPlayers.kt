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

sealed class ModoDerepeticao(val valor:Int){
    object Desativado:ModoDerepeticao(0)
    object RepetirEssa:ModoDerepeticao(1)
    object RepetirTodos:ModoDerepeticao(2)

    override fun toString(): String {
        when(this){
            is Desativado->{
                return "Desativado"
            }
            is RepetirEssa->{
                return "RepetirEssa"}
            is RepetirTodos->{
                return "RepetirTodos"

            }
        }

    }
}