package com.example.songs.navegacao

sealed class DestinosDENavegacao(val rota:String) {
       object Player : DestinosDENavegacao("player")
       object Todas: DestinosDENavegacao("todas")
       object Playlist: DestinosDENavegacao("playlist")
       object Album: DestinosDENavegacao("album")
       object Buscador: DestinosDENavegacao("buscador")
       object Configuracoes: DestinosDENavegacao("configuracoes")

}