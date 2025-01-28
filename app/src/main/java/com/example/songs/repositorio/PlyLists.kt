package com.example.songs.repositorio

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileNotFoundException
import kotlin.jvm.Throws

@OptIn(ExperimentalStdlibApi::class)
suspend fun checarCorotina():Boolean{
    if(!Dispatchers.IO.equals(currentCoroutineContext()[CoroutineDispatcher])) throw(RuntimeException("corrotina fora do contexto IO"))
    return true
}
class PlyLists(private val context: Context):InterfacePlylist {
    private final val extencao=".sng"




  suspend  private fun criarArquivo(nome :String):Boolean{
        try {

            val file =File(context.filesDir,nome)
            if(!file.exists()) file.createNewFile()
            else return false
            return true
        }catch (e:Exception){
            return false
        }

        return  false
    }
   override suspend fun criarPlyList(nome:String){

       checarCorotina()
       val criado=  criarArquivo(nome+extencao)
       if(!criado) Toast.makeText(context,"nao foi posivla criar a PlyList $nome",Toast.LENGTH_SHORT).show()
       else Toast.makeText(context,"PlyList $nome criada com sucesso",Toast.LENGTH_SHORT).show()

    }

   override suspend fun criarPlaylist(nome: String,mediaItem: MediaItem){
        checarCorotina()
        val metadata= mediaItem.mediaMetadata
        val criado=  criarArquivo(nome+extencao)
        if(!criado){
            Toast.makeText(context,"nao foi posivla criar a PlyList $nome",Toast.LENGTH_SHORT).show()
            return}
        val imput = context.openFileOutput(nome,Context.MODE_PRIVATE).use {
            it.write("#${metadata.title.toString()},${metadata.artist.toString()},${metadata.albumArtist.toString()},${metadata.artworkUri.toString()},${mediaItem.mediaId},\n".toByteArray())

        }


        }

   override suspend fun adicionarAplyList(nome: String,mediaItem: MediaItem){
        checarCorotina()
        var string = "#${mediaItem.mediaMetadata.title.toString()},${mediaItem.mediaMetadata.artist.toString()},${mediaItem.mediaMetadata.albumArtist.toString()},${mediaItem.mediaMetadata.artworkUri.toString()},${mediaItem.mediaId},\n}"
        val file= File(context.filesDir,nome+extencao)
        if(!file.exists()) throw FileNotFoundException(" $nome nao encontrado")
        val imput = context.openFileOutput(nome,Context.MODE_APPEND).use {
            it.write(string.toByteArray())
        }
    }

   override suspend fun removerPlaylist(nome: String){
        checarCorotina()
        val file= File(context.filesDir,nome+extencao)
       Log.i("TAG", "removerPlaylist: ${nome+extencao}")
        if(file.exists())
            file.delete()
        else throw FileNotFoundException(" $nome nao encontrado")
    }

    override suspend fun listarArquivo(
        nome: String,

    ): List<MediaItem> {
        checarCorotina()
        val list= mutableListOf("")
        val file= File(context.filesDir,nome+extencao)
       context.openFileInput(nome+extencao).bufferedReader().useLines {
              list.add( it.fold(""){some,text->
                  "$some\n$text"

              })
           }
      val l= list.map {
         val aux=  it.split("#")
         val aux2=aux[1].split(",")
           MediaItem.Builder().setUri(aux2[3])
               .setMediaId(aux2[4])
               .setMediaMetadata(MediaMetadata.Builder().setTitle(aux2[0])
                                                        .setArtist(aux2[1])
                                                        .setAlbumArtist(aux2[3])
                                                        .build()).build()
       }

        return l
    }

   override fun listaPlaylist(): Flow<List<String>> = flow{

    while (true)
        try {

            emit(context.fileList().toList())
            delay(2000)
        }catch (e:Exception){
            emit(emptyList())
            delay(2000)
        }





    }
}