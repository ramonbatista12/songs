package com.songsSongs.songs.componentes

import com.songsSongs.songs.R
import com.songsSongs.songs.navegacao.DestinosDENavegacao
/*
* classe responsavel por reprensetar os icones da barra superior e da bara de navegacao
*
* */
sealed class Icones(val rota:DestinosDENavegacao,val icone:Int){
    object Todas:Icones(rota=DestinosDENavegacao.DestinosDeTela.Todas, icone = R.drawable.iconetodasasmusicas2)
    object PlayList:Icones(rota=DestinosDENavegacao.DestinosDeTela.Playlist, icone = R.drawable.baseline_playlist_play_24)
    object Album:Icones(rota = DestinosDENavegacao.DestinosDeTela.Album, icone = R.drawable.baseline_album_24)
    object Equalizacao:Icones(rota = DestinosDENavegacao.DestinosDeTela.Configuracoes,R.drawable.iconeequalizacao2)
    object Artista:Icones(rota = DestinosDENavegacao.DestinosDeTela.Artista, icone = R.drawable.baseline_artistas_24)


   companion object {
       val list:List<Icones> = listOf(Todas,PlayList,Equalizacao)
       val listBotoesParaSelecaoDePlyList= listOf(PlayList,Album,Artista)
   }

    override fun toString(): String {
        return when(this){
            Todas->"Todas"
            PlayList->"PlayList"
            Album->"Album"
            Artista->"Artista"
            Equalizacao->"Configuracoes"}
    }


}





