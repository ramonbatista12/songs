package com.example.songs.repositorio

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface InterfacePlylist {
  suspend  fun criarPlyList(nome:String)
  suspend  fun criarPlaylist(nome: String,mediaItem: MediaItem)
  suspend fun adicionarAplyList(nome: String,mediaItem: MediaItem)
  suspend fun removerPlaylist(nome: String)
  fun listaPlaylist(): Flow<List<String>>
  suspend fun listarArquivo(nome: String):List<MediaItem>

}