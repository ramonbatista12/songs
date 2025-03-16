package com.songsSongs.songs.repositorio

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface InterfacePlylist {
  suspend  fun criarPlyList(nome:String)
  suspend  fun criarPlaylist(nome: String,mediaItem: MediaItem)
  suspend fun adicionarAplyList(idPlylist:Long,mediaItem: MediaItem)
  suspend fun removerPlaylist(idPlylist: Long)
  suspend fun removerItemDaPlaylist(idPlylist: Long)
  suspend fun atualizarPlylist(plylist:ListaPlaylist)
  suspend fun removerItemDaPlyList(idMedia:String)
  suspend fun tumbmails(id: Long): List<ImagemTumbmail>
  fun listaPlaylist(): Flow<List<ListaPlaylist>>
  fun mediaItemsDaPlylist(idPlylist: Long):Flow<List<ItemsDeMedia>>


}