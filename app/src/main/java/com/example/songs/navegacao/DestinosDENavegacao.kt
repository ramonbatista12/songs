package com.example.songs.navegacao

import android.net.Uri
import com.example.songs.repositorio.Artista
import kotlinx.serialization.Serializable

/*
* a classe DestinosDENavegacao serve para representar os diferentes destinos no grafico
* quakquer novo destino adicionado deveser adicionado a essa classe por dois motivos
* 1:  e uma boa pratica
* 2:todos os destinos de navegacao estao aqui e devem ser pasados pr essa classe permitindo que ela seja a unica fonte de
* rotas para cada destino no grafico de navegacao
* obs: anova api de navegao indica a pasagem de objetos para representar os destinos
* cada objet deve ser serializavel
* */
sealed class DestinosDENavegacao {

sealed class DestinosDeTela:DestinosDENavegacao(){
      @Serializable
      object Player:DestinosDENavegacao()
      @Serializable
      object Todas:DestinosDENavegacao()
      @Serializable
      object Playlist:DestinosDENavegacao()
      @Serializable
      object Album:DestinosDENavegacao()
      @Serializable
      class Buscador:DestinosDENavegacao()
      @Serializable
      object Artista:DestinosDENavegacao()
      @Serializable
      class ArtistaId(val id:Long):DestinosDENavegacao()
      @Serializable
      class AlbumId(val id: Long):DestinosDENavegacao()
      @Serializable
      data class PlyListId(val id:Long):DestinosDENavegacao()

      @Serializable
      object Configuracoes:DestinosDENavegacao()
  }

    sealed class DestinosDeDialogo:DestinosDENavegacao(){

        @Serializable
        class AdiconarPlaylist(val titulo:String="",
                               val uri: String="",
                               val artista: String="",
                               val album:String="",
                               val id:String="",
                               val duracao:String=""):DestinosDeDialogo()

        @Serializable
        data class OpcoesItemsDaLista(val titulo:String="",
                                      val uri: String="",
                                      val artista: String="",
                                      val album:String="",
                                      val id:String="",
                                      val duracao:String=""):DestinosDeDialogo()

        @Serializable
        data class OpcoesItemsAlbums(val idDoAlbum:Long):DestinosDeDialogo()

        @Serializable
        object CriarPlaylist:DestinosDeDialogo()

    }
}
