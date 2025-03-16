package com.songsSongs.songs.repositorio

sealed class Resultado{
    class Ok<out T>(val result:T=Unit as T):Resultado()
    class Erro(val mensagem:String):Resultado()
}