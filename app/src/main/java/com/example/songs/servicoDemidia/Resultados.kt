package com.example.songs.servicoDemidia

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.session.MediaController
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.SessionToken
import androidx.media3.session.legacy.MediaSessionCompat.Token
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

sealed class ResultadosConecaoServiceMedia{
    class Conectado (val setvice:ServicMedia,private val c: Context): ResultadosConecaoServiceMedia(){
        var controle: MediaController?=null
        val scop= CoroutineScope(Dispatchers.Main)
        init {
            scop.launch {
             /* val future=MediaController.Builder(c,SessionToken(c, ComponentName(c,ServicMedia::class.java))).buildAsync()
                future.addListener({
                    if(future.isDone)
                    controle=future.get()
                    else controle=null
                    if(future.isCancelled)
                        Log.i("future","foicancelada")
                }, MoreExecutors.directExecutor())*/


            }
        }
    }
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