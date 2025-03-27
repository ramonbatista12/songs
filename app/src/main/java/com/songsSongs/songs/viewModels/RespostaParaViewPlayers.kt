package com.songsSongs.songs.viewModels

import android.graphics.Bitmap
import androidx.media3.common.MediaItem
import com.songsSongs.songs.R
import com.songsSongs.songs.repositorio.Album
import com.songsSongs.songs.repositorio.Artista

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

sealed class ListaAlbums(){
    object caregando:ListaAlbums()
    object Vasia:ListaAlbums()
    class Lista(val lista:List<Album>):ListaAlbums()

}

sealed class ListaArtists(){
    object caregando:ListaAlbums()
    object Vasia:ListaAlbums()
    class Lista(val lista:List<Artista>):ListaAlbums()

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

sealed class ImagemPlyer{

    data class Vazia(val icone:Int= R.drawable.inomeado):ImagemPlyer()
    data class Imagem(val imagem:Bitmap):ImagemPlyer()


}