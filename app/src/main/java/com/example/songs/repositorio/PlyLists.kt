package com.example.songs.repositorio

import android.content.Context
import android.widget.Toast
import androidx.media3.common.MediaItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import kotlin.jvm.Throws

@OptIn(ExperimentalStdlibApi::class)
suspend fun checarCorotina():Boolean{
    return Dispatchers.IO.equals(currentCoroutineContext()[CoroutineDispatcher])
}
class PlyLists(private val context: Context) {
  suspend  private fun criarArquivo(nome :String):Boolean{

        val file =File(context.filesDir,nome).createNewFile()
        return  file
    }
    suspend fun criarPlyList(nome:String){

       if(!checarCorotina()) throw(RuntimeException("corrotina fora do contexto IO"))
       val criado=  criarArquivo(nome)
       if(!criado) Toast.makeText(context,"nao foi posivla criar a PlyList $nome",Toast.LENGTH_SHORT).show()
       else Toast.makeText(context,"PlyList $nome criada com sucesso",Toast.LENGTH_SHORT).show()

    }

    suspend fun criarPlaylist(nome: String,mediaItem: MediaItem){
        if(!checarCorotina()) throw(RuntimeException("corrotina fora do contexto IO"))
        val metadata= mediaItem.mediaMetadata
        val criado=  criarArquivo(nome)
        if(!criado){
            Toast.makeText(context,"nao foi posivla criar a PlyList $nome",Toast.LENGTH_SHORT).show()
            return}
        val imput = context.openFileOutput(nome,Context.MODE_APPEND).use {
            it.write("#${metadata.title.toString()}, ${metadata.artist.toString()},${metadata.albumArtist.toString()},${metadata.artworkUri.toString()},${mediaItem.mediaId},\n".toByteArray())

        }


        }





    fun listaPlaylist(): Flow<List<String>> = flow{

         while (true){


            emit(context.fileList().toList())
             delay(3000)
         }
    }
}