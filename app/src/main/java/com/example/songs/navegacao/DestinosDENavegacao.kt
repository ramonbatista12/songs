package com.example.songs.navegacao
/*
* a classe DestinosDENavegacao serve para representar os diferentes destinos no grafico
* quakquer novo destino adicionado deveser adicionado a essa classe por dois motivos
* 1:  e uma boa pratica
* 2:todos os destinos de navegacao estao aqui e devem ser pasados pr essa classe permitindo que ela seja a unica finte de
* rotas para cada destinoi no grafico de navegacao
* */
sealed class DestinosDENavegacao(val rota:String) {
       object Player : DestinosDENavegacao("player")
       object Todas: DestinosDENavegacao("todas")
       object Playlist: DestinosDENavegacao("playlist")
       object Album: DestinosDENavegacao("album")
       object Buscador: DestinosDENavegacao("buscador")
       object Configuracoes: DestinosDENavegacao("configuracoes")

}