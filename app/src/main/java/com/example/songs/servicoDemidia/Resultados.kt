package com.example.songs.servicoDemidia

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.legacy.MediaSessionCompat.Token

sealed class ResultadosConecaoServiceMedia{
    class Conectado (val setvice:ServicMedia ): ResultadosConecaoServiceMedia()
    object Desconectado: ResultadosConecaoServiceMedia()
    class Erro(val mensage:String): ResultadosConecaoServiceMedia()

    override fun toString(): String {

        return when(this){
            is Conectado->"Conectado"
            is Desconectado->"Desconectado"
            is Erro->"Erro $mensage"
        }
    }

}