package com.example.songs.repositorio

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@RequiresApi(Build.VERSION_CODES.Q)
class RepositorioService(val context: Context) {
    private val job= Job()
    private val scope= CoroutineScope(Dispatchers.IO)
    private val listaDeMediaItemsTodadasAsMusicas= MutableStateFlow<List<MediaItem>>(emptyList())
    var _listaDeMediaItems=listaDeMediaItemsTodadasAsMusicas.asStateFlow()
    private val listaDeAlbums= mutableListOf<MediaItem>()
    private val listaDeArtistas= mutableListOf<MediaItem>()
    val listaDeMusicas= mutableListOf<MediaItem>()
    val listaDeMusicasAlbum= mutableListOf<MediaItem>()



    init {
        scope.launch {
            getMusics().collect{
                listaDeMediaItemsTodadasAsMusicas.emit(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(UnstableApi::class)
   private fun getMusics()= flow<List<MediaItem>>{
        val contentResolver=context.contentResolver
        val listaDeMediaItems= mutableListOf<MediaItem>()
        val projeca= arrayOf<String>(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media._ID

        )

       val ordenacao ="${MediaStore.Audio.Media.DISPLAY_NAME} ASC "
       scope.launch(Dispatchers.IO) { val cursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                         projeca,
                                null,
                             null,
                                        ordenacao).use { cursor->

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
                                                                       val bitmap= getMetaData(uri,cursor.getLong(id))
                                                                       this.setArtist(cursor.getString(artista))
                                                                       this.setDurationMs(cursor.getLong(duracao))
                                                                   }.build()
                                                               ).build()
                            listaDeMediaItems.add(mediaItem)


                          }
                                        }
               val listaEmicao=listaDeMediaItems.toList()
               emit(listaEmicao)
        }

    }

 @RequiresApi(Build.VERSION_CODES.Q)
 suspend fun getMetaData(uri: Uri, id: Long):Bitmap{
    val resolver = this.context.contentResolver
    val tumbmail=resolver.loadThumbnail(uri,Size(100,100),null)
    return tumbmail
 }


}