package com.example.songs.servicoDemidia

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.session.MediaController
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.media3.session.legacy.MediaSessionCompat.Token
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

sealed class ResultadosConecaoServiceMedia{
    class Conectado (val setvice:ServicMedia,private val c: Context): ResultadosConecaoServiceMedia()
    object Desconectado: ResultadosConecaoServiceMedia()
    class Erro(val mensage:String): ResultadosConecaoServiceMedia()

    override fun toString(): String {

        return when(this){
            is Conectado->"Conectado"
            is Desconectado->"Desconectado"
            is Erro->"Erro $mensage"
            else -> {""}
        }
    }

}

sealed class StadosDoPlaye(){
    object Emplyer: StadosDoPlaye()
    object Caregando: StadosDoPlaye()
    object Erro: StadosDoPlaye()

    override fun toString(): String {
        when(this){
            is Emplyer->return "Exoplyer esta reproduzindo"
            is Caregando->return "Exoplyer esta Caregando"
            is Erro->return "Exoplyer esta com Erro na Reproducao"
            else -> return ""
        }
    }
}

sealed class PlyListStados(){
    object Todas: PlyListStados()
    class Artista(val artistaId:Long): PlyListStados()
    class Album(val albumId:Long): PlyListStados()
    class Playlist(val playlistId:Long): PlyListStados()

    override fun toString(): String {
        when(this){
            is Todas->return "Todas"
            is Artista->return "Artista $artistaId"
            is Album->return "Album $albumId"
            is Playlist->return "Playlist $playlistId"
            else -> return ""

        }
    }



}