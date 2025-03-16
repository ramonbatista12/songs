package com.songsSongs.songs.componentes

import com.songsSongs.songs.R
import com.songsSongs.songs.navegacao.DestinosDENavegacao
/*
* classe responsavel por reprensetar os icones da barra superior e da bara de navegacao
*
* */
sealed class Icones(val rota:DestinosDENavegacao,val icone:Int){
    object Todas:Icones(rota=DestinosDENavegacao.DestinosDeTela.Todas, icone = R.drawable.baseline_list_24)
    object PlayList:Icones(rota=DestinosDENavegacao.DestinosDeTela.Playlist, icone = R.drawable.baseline_playlist_play_24)
    object Album:Icones(rota = DestinosDENavegacao.DestinosDeTela.Album, icone = R.drawable.baseline_album_24)
    object Configuracoes:Icones(rota = DestinosDENavegacao.DestinosDeTela.Configuracoes,R.drawable.baseline_build_circle_24)
    object Artista:Icones(rota = DestinosDENavegacao.DestinosDeTela.Artista, icone = R.drawable.baseline_artistas_24)


   companion object {
       val list:List<Icones> = listOf(Todas,PlayList,Album,Artista/*,Configuracoes*/)

   }



}



