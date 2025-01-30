package com.example.songs.repositorio

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface InterfacePlylist {
  suspend  fun criarPlyList(nome:String)
  suspend  fun criarPlaylist(nome: String,mediaItem: MediaItem)
  suspend fun adicionarAplyList(idPlylist:Long,mediaItem: MediaItem)
  suspend fun removerPlaylist(idPlylist: Long)
  suspend fun removerItemDaPlaylist(idPlylist: Long)
  fun listaPlaylist(): Flow<List<ListaPlaylist>>
  suspend fun mediaItemsDaPlylist(idPlylist: Long):Flow<List<ItemsDeMedia>>

}