package com.example.songs.repositorio

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@RequiresApi(Build.VERSION_CODES.Q)
class RepositorioService(val context: Context) {






    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(UnstableApi::class)
   fun getMusics()= flow<List<MediaItem>>{

        val contentResolver=context.contentResolver

        val projeca= arrayOf<String>(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media._ID

        )

       val ordenacao ="${MediaStore.Audio.Media.DISPLAY_NAME} ASC "
            val storege = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

          while (true){
          val listaDeMediaItems= mutableListOf<MediaItem>()
           val cursor=contentResolver.query(storege,
                                            projeca,
                                    null,
                                null,
                                            ordenacao,null).use { cursor->

                             val nome=cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                             val artista=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                             val album=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                             val duracao=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                             val id=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                          while (cursor.moveToNext()){
                              val uri =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getLong(id))
                              val mediaItem=MediaItem.Builder().setMediaId("${cursor.getLong(id)}")
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
                            Log.d("TAG", "getMusics: ${cursor.getString(nome)}")

                          }

                                        }


        emit(listaDeMediaItems)
              Log.d("TAG","lista emitida")
              delay(2000)
          }



    }


   fun getAlbums()= flow<List<Album>> {
       val projecao= arrayOf<String>(
           MediaStore.Audio.Albums.ALBUM,
           MediaStore.Audio.Albums.ARTIST,
           MediaStore.Audio.Albums.ALBUM_ID,
           MediaStore.Audio.Albums._ID
       )


       val ordenacao= "${MediaStore.Audio.Media.ALBUM} ASC"
       while (true){
       val listaDeAlbums= mutableListOf<Album>()
       val cursor=context.contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
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
                                                                                                       it.getLong(id_)).toString()))
       }
           emit(listaDeAlbums)
       delay(4000)
       }


   }

  fun getArtistas()= flow<List<Artista>>{
      val projecao= arrayOf<String>(
          MediaStore.Audio.Artists.ARTIST,
          MediaStore.Audio.Artists._ID,

      )
      val listaDeArtistas= mutableListOf<Artista>()

      val ordenacao="${MediaStore.Audio.Artists.ARTIST} ASC"
      while (true){
      val  cursor=context.contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,projecao,null,null,ordenacao).use {
          val id=it!!.getColumnIndexOrThrow(projecao[0])
          val artista=it.getColumnIndexOrThrow(projecao[1])

           while (it!!.moveToNext()){
               listaDeArtistas.add(Artista(it.getLong(artista),it.getString(id)))
           }


      }
      emit(listaDeArtistas)
      delay(4000)

      }







  }


 @RequiresApi(Build.VERSION_CODES.Q)
 suspend  fun getMetaData(uri: Uri, id: Long):Bitmap?{
   try {
       val resolver = this.context.contentResolver
       val tumbmail=resolver.loadThumbnail(uri,Size(100,100),null)
       return tumbmail
   }catch (e:Exception){
       return null
   }

 }


}