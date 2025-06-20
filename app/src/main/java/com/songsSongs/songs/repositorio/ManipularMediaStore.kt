package com.songsSongs.songs.repositorio

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ManipularMediaStore(val context: Context):InterfasseMediaStore  {
    @OptIn(UnstableApi::class)
    override fun getMusics(): Flow<List<MediaItem>> = flow<List<MediaItem>>{

        val contentResolver=context.contentResolver

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
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        while (true){
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


            emit(listaDeMediaItems)
            //     Log.d("TAG","lista emitida")
            delay(2000)
        }



    }

    override fun getAlbums(): Flow<List<Album>> = flow<List<Album>> {
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
                            it.getLong(id_))))
            }
            emit(listaDeAlbums)
            delay(4000)
        }


    }

    override fun getArtistas(): Flow<List<Artista>> =  flow<List<Artista>>{
        val projecao= arrayOf<String>(
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists._ID,

            )


        val ordenacao="${MediaStore.Audio.Artists.ARTIST} ASC"
        while (true){
            val listaDeArtistas= mutableListOf<Artista>()
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

    override fun getMusicasPorArtista(id: Long): Flow<List<MediaItem>> =flow<List<MediaItem>>{
        val contentResolver=context.contentResolver
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
        while (true){
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
            emit(listaDeMediaItems)
            delay(4000)
        }

    }

    override fun getMusicasPorAlbum(id: Long): Flow<List<MediaItem>> = flow<List<MediaItem>>{

        val contentResolver=context.contentResolver
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
        while (true){
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
                emit(listaDeMediaItems)
                delay(2000)

            }
        }
    }
}