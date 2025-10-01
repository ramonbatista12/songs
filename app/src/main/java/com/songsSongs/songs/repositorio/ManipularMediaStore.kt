package com.songsSongs.songs.repositorio

import android.content.ContentUris
import android.content.Context
import android.database.ContentObservable
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

class ManipularMediaStore(private var context: Context?):InterfasseMediaStore  {
    private val validador=ValidadorMEiaStore(context!!)
    @OptIn(UnstableApi::class)
    override fun getMusics(): Flow<List<MediaItem>> = flow<List<MediaItem>>{
        coroutineScope {
            val canalDeAviso =Channel<Unit>(Channel.CONFLATED)
            val observasor = object : Invalidacao {
                override fun Invalidacao() {
                    canalDeAviso.trySend(Unit)
                }
            }
            validador.addObServador(observasor)
            canalDeAviso.trySend(Unit)
            val canalEnvio = Channel<List<MediaItem>>()
              launch {
                  try {
                      for (aviso in canalDeAviso){
                          Log.i("calnal de aviso ","litagem de musica recebeu um sina")
                          val lista=listarMusicas()
                          canalEnvio.send(lista)

                      }
                  }catch (e:Exception){
                      e.printStackTrace()
                      validador.removeObServador(observasor)
                  }



              }
            emitAll(canalEnvio)
            }
        }
    override fun getAlbums(): Flow<List<Album>> = flow<List<Album>> {
       coroutineScope {
           val canalDeAviso= Channel<Unit>(Channel.CONFLATED)
           val observador=object :Invalidacao{
               override fun Invalidacao() {
                   Log.i("canal de aviso ","album recebeu um sinal em get albums")
                   canalDeAviso.trySend(Unit)
               }
               }
           canalDeAviso.trySend(Unit)
           val canalRespsota = Channel<List<Album>>()
           validador.addObServador(observador)
           launch {
               try {

                   for (aviso in canalDeAviso){
                       val listaDeAlbums=listarAlbums()
                       canalRespsota.send(listaDeAlbums)
                   }
               }finally {
                   validador.removeObServador(observador)

               }

           }
           emitAll(canalRespsota)
       }
       }

     override fun getArtistas(): Flow<List<Artista>> =  flow<List<Artista>>{
        coroutineScope {
            val canalDeAviso= Channel<Unit>(Channel.CONFLATED)
            val observador=object :Invalidacao{
                override fun Invalidacao() {
                    Log.i("canal de aviso ","album recebeu um sinal em get Artistas")
                    canalDeAviso.trySend(Unit)
                }
            }
            canalDeAviso.trySend(Unit)
            val canalRespsota = Channel<List<Artista>>()
            launch {
                try {
                    validador.addObServador(observador)
                    for (aviso in canalDeAviso){
                        val lista=listarArtistas()
                        canalRespsota.send(lista)
                    }
                }finally {
                    validador.removeObServador(observador)

                }

            }
            emitAll(canalRespsota)
        }







    }

    override fun getMusicasPorArtista(id: Long): Flow<List<MediaItem>> =flow<List<MediaItem>>{
        coroutineScope {
            val canalDeAviso= Channel<Unit>(Channel.CONFLATED)
            val observador=object :Invalidacao{
                override fun Invalidacao() {
                    Log.i("canal de aviso ","album recebeu um sinal em get Artistas Por idArista")
                    canalDeAviso.trySend(Unit)
                }
            }
            canalDeAviso.trySend(Unit)
            val canalRespsota = Channel<List<MediaItem>>()
            launch {
                try {
                    validador.addObServador(observador)
                    for (aviso in canalDeAviso){
                        val lista=listarmusicasPorArtista(id)
                        canalRespsota.send(lista)
                    }
                }finally {
                    validador.removeObServador(observador)

                }

            }
            emitAll(canalRespsota)
        }

    }

    @OptIn(UnstableApi::class)
    override fun getMusicasPorAlbum(id: Long): Flow<List<MediaItem>> = flow<List<MediaItem>>{
        coroutineScope {
            val canalDeAviso= Channel<Unit>(Channel.CONFLATED)
            val observador=object :Invalidacao{
                override fun Invalidacao() {
                    Log.i("canal de aviso ","album recebeu um sinal em get MusicasPorAlbum")
                    canalDeAviso.trySend(Unit)
                }
            }
            canalDeAviso.trySend(Unit)
            val canalRespsota = Channel<List<MediaItem>>()
            launch {
                try {
                    validador.addObServador(observador)
                    for (aviso in canalDeAviso){
                        val lista=listarMusicasPorAlbum(id)
                         canalRespsota.send(lista)
                    }
                }finally {
                    validador.removeObServador(observador)

                }

            }
            emitAll(canalRespsota)
        }

    }

    private fun listarMusicasPorAlbum(id:Long):List<MediaItem>{
        val contentResolver=context!!.contentResolver
        val projecao=  arrayOf<String>(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID)
        val ordenacao ="${MediaStore.Audio.Media.DISPLAY_NAME} ASC "
        val storege = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val listaDeMediaItems= mutableListOf<MediaItem>()
            val cursor=contentResolver.query(storege,
                projecao,
                "${MediaStore.Audio.Media.ALBUM_ID} =? ",
                arrayOf(id.toString()),
                ordenacao,null).use { cursor->

                val nome=cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artista=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val album=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val duracao=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val idm=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                while (cursor.moveToNext()){
                    val uri =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getLong(idm))
                    val mediaItem=MediaItem.Builder().setUri(uri).setMediaId("${cursor.getLong(idm)}")
                        .setMediaMetadata(
                            MediaMetadata.Builder().apply {
                                this.setTitle(cursor.getString(nome))
                                this.setAlbumArtist(cursor.getString(album))
                                this.setArtworkUri(uri)
                                //val bitmap= getMetaData(uri,cursor.getLong(id))
                                this.setArtist(cursor.getString(artista))
                                this.setDurationMs(cursor.getLong(duracao))
                            }.build()
                        ).build()
                    listaDeMediaItems.add(mediaItem)



                }
                return listaDeMediaItems



        }
    }
    private fun listarMusicas():List<MediaItem>{
    val contentResolver=context!!.contentResolver

    val projeca= arrayOf<String>(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media._ID

    )

    val ordenacao ="${MediaStore.Audio.Media.TITLE} ASC "
    val storege = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    }
    else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI


    val listaDeMediaItems= mutableListOf<MediaItem>()
    val cursor=contentResolver.query(storege,
        projeca,
        null,
        null,
        ordenacao,null).use { cursor->

        val nome=cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artista=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val album=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val duracao=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val id=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

        while (cursor.moveToNext()){
            val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getLong(id))
            val mediaItem=MediaItem.Builder().setUri(uri).setMediaId("${cursor.getLong(id)}")
                .setMediaMetadata(
                    MediaMetadata.Builder().apply {
                        this.setTitle(cursor.getString(nome))
                        this.setAlbumArtist(cursor.getString(album))
                        this.setArtworkUri(uri)
                        //val bitmap= getMetaData(uri,cursor.getLong(id))
                        this.setArtist(cursor.getString(artista))
                        this.setDurationMs(cursor.getLong(duracao))
                    }.build()
                ).build()
            listaDeMediaItems.add(mediaItem)
            //    Log.d("TAG", "getMusics: ${cursor.getString(nome)}")

        }

    }

        return listaDeMediaItems

}
    private fun listarAlbums():List<Album>{
       val projecao= arrayOf<String>(
       MediaStore.Audio.Albums.ALBUM,
       MediaStore.Audio.Albums.ARTIST,
       MediaStore.Audio.Albums.ALBUM_ID,
       MediaStore.Audio.Albums._ID
   )


       val ordenacao= "${MediaStore.Audio.Media.ALBUM} ASC"

           val listaDeAlbums= mutableListOf<Album>()
           val cursor=context!!.contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
               projecao,
               null,
               null,
               ordenacao).use {
               val album=it!!.getColumnIndexOrThrow(projecao[0])
               val artista=it.getColumnIndexOrThrow(projecao[1])
               val id=it.getColumnIndexOrThrow(projecao[2])
               val id_=it.getColumnIndexOrThrow(projecao[3])
               while (it!!.moveToNext())
                   listaDeAlbums.add(Album(it.getLong(id),
                       it.getString(album),
                       it.getString(artista),
                       ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                           it.getLong(id_))))
           }
           return listaDeAlbums



   }
    private fun listarArtistas():List<Artista>{
        val projecao= arrayOf<String>(
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists._ID,

            )


        val ordenacao="${MediaStore.Audio.Artists.ARTIST} ASC"

            val listaDeArtistas= mutableListOf<Artista>()
            val  cursor=context!!.contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,projecao,null,null,ordenacao).use {
                val id=it!!.getColumnIndexOrThrow(projecao[0])
                val artista=it.getColumnIndexOrThrow(projecao[1])

                while (it!!.moveToNext()){
                    listaDeArtistas.add(Artista(it.getLong(artista),it.getString(id)))
                }


            }
            return listaDeArtistas



    }
    private fun listarmusicasPorArtista(id:Long):List<MediaItem>{
        val contentResolver=context!!.contentResolver
        val projecao=  arrayOf<String>(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST_ID)
        val ordenacao ="${MediaStore.Audio.Media.TITLE} ASC "
        val storege = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val listaDeMediaItems= mutableListOf<MediaItem>()
            val cursor=contentResolver.query(storege,
                projecao,
                "${MediaStore.Audio.Media.ARTIST_ID}=? ",
                arrayOf(id.toString()),
                ordenacao,null).use { cursor->

                val nome=cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artista=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val album=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val duracao=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val id=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                while (cursor.moveToNext()){
                    val uri =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getLong(id))
                    val mediaItem=MediaItem.Builder().setUri(uri).setMediaId("${cursor.getLong(id)}")
                        .setMediaMetadata(
                            MediaMetadata.Builder().apply {
                                this.setTitle(cursor.getString(nome))
                                this.setAlbumArtist(cursor.getString(album))
                                this.setArtworkUri(uri)
                                //val bitmap= getMetaData(uri,cursor.getLong(id))
                                this.setArtist(cursor.getString(artista))
                                this.setDurationMs(cursor.getLong(duracao))
                            }.build()
                        ).build()
                    listaDeMediaItems.add(mediaItem)


                }

            }
            return listaDeMediaItems


    }
    fun finalizar(){this.context=null}
}


class ValidadorMEiaStore(private val context: Context){
    private val listaDeObServadores = CopyOnWriteArrayList<Invalidacao>()
    private val coroutinesScope= CoroutineScope(Dispatchers.IO)
    private val verificacao =verificacao()

    init {
        coroutinesScope.launch {
            verificacao.collect{
                coroutinesScope.launch {
                    listaDeObServadores.forEach {
                        it.Invalidacao()
                    }
                }


        }
    }}

    suspend fun addObServador(invalidacao: Invalidacao){
        listaDeObServadores.add(invalidacao)
    }
    suspend fun removeObServador(invalidacao: Invalidacao){
        listaDeObServadores.remove(invalidacao)
    }
     fun verificacao()= callbackFlow<Unit> {
         val objetoInvalidador = object :  ContentObserver(Handler(Looper.getMainLooper())) {
             override fun onChange(selfChange: Boolean) {
                 super.onChange(selfChange)
                 Log.d("invalidaor", "onChange: recebeu um sinal de invalidação em ${MediaStore.Audio.Media.EXTERNAL_CONTENT_URI} ")
                 trySend(Unit)
             }}
         val contentResolver=context.contentResolver.registerContentObserver( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,true,objetoInvalidador )
       awaitClose {
           context.contentResolver.unregisterContentObserver(objetoInvalidador)
       }
     }
}

interface Invalidacao{
    fun Invalidacao()
}