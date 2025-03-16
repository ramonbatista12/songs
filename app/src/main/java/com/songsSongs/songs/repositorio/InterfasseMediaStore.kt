package com.songsSongs.songs.repositorio

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface InterfasseMediaStore {
    fun getMusics():Flow<List<MediaItem>>
    fun getAlbums(): Flow<List<Album>>
    fun getArtistas():Flow<List<Artista>>
    fun getMusicasPorArtista(id:Long):Flow<List<MediaItem>>
    fun getMusicasPorAlbum(id:Long):Flow<List<MediaItem>>

}