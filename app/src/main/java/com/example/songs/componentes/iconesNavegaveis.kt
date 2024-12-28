package com.example.songs.componentes

import android.graphics.drawable.Drawable
import com.example.songs.R
import com.example.songs.navegacao.DestinosDENavegacao

sealed class Icones(val rota:DestinosDENavegacao,val icone:Int){
    object Todas:Icones(rota=DestinosDENavegacao.Todas, icone = R.drawable.baseline_list_24)
    object PlayList:Icones(rota=DestinosDENavegacao.Playlist, icone = R.drawable.baseline_playlist_play_24)
    object Album:Icones(rota = DestinosDENavegacao.Album, icone = R.drawable.baseline_album_24)
    object Configuracoes:Icones(rota = DestinosDENavegacao.Configuracoes,R.drawable.baseline_build_circle_24)
    object Artista:Icones(rota = DestinosDENavegacao.Artista, icone = R.drawable.baseline_artistas_24)


   companion object {val list:List<Icones> = listOf(Todas,PlayList,Album,Configuracoes,Artista)}



}



