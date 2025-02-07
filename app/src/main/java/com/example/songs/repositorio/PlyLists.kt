package com.example.songs.repositorio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext

import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalStdlibApi::class)
suspend fun checarCorotina():Boolean{
  // if(!Dispatchers.IO.equals(currentCoroutineContext()[CoroutineDispatcher]))// throw(RuntimeException("corrotina fora do contexto IO"))
    return true
}
class PlyLists(private val context: Context):InterfacePlylist {

   private val roomDatabase by lazy{
       Room.databaseBuilder(context,RoomBd::class.java,"bd").build()
   }
   private val dao by lazy {
       roomDatabase.dao()
   }




    override suspend fun criarPlyList(nome: String) {
       dao.inserirPlylist(ListaPlaylist(0,nome))
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    override suspend fun criarPlaylist(nome: String, mediaItem: MediaItem) {
        dao.inserirPlylist(ListaPlaylist(0,nome))
       val plylist= dao.plyListCriada()
       val id=plylist?.id ?: 0
       val metadata=mediaItem.mediaMetadata
       dao.inserirItemDeMedia(ItemsDeMedia(
            id=0,
            idPlylist = id.toLong(),
            titulo = metadata.title.toString(),
            album = metadata.albumArtist.toString(),
            uri = metadata.artworkUri.toString(),
            idMedia = mediaItem.mediaId.toString(),
            artista = metadata.artist.toString(),
            duracao = metadata.durationMs!!.toLong())
            )
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    override suspend fun adicionarAplyList(idPlylist: Long, mediaItem: MediaItem) {
        val metadata=mediaItem.mediaMetadata
        dao.inserirItemDeMedia(ItemsDeMedia(
            id=0,
            idPlylist = idPlylist,
            titulo = metadata.title.toString(),
            album = metadata.albumArtist.toString(),
            uri = metadata.artworkUri.toString(),
            idMedia = mediaItem.mediaId.toString(),
            artista = metadata.artist.toString(),
            duracao = metadata.durationMs!!.toLong())
        )
    }

    override suspend fun removerPlaylist(id: Long) {
        dao.deletarPlylist(ListaPlaylist(id=id,nome = ""))
    }

    override suspend fun removerItemDaPlaylist(idPlylist: Long) {
        dao.removerItemDeMedia(idPlylist.toString())
    }

    override suspend fun atualizarPlylist(plylist: ListaPlaylist) =dao.atualizarPlylist(plylist)
    override suspend fun removerItemDaPlyList(idMedia: String) = dao.removerItemDeMedia(idMedia)
    override fun listaPlaylist(): Flow<List<ListaPlaylist>> = dao.fluxoPlyList()

    override  fun mediaItemsDaPlylist(id: Long): Flow<List<ItemsDeMedia>> =dao.fluxoDeItemsDeMedia(id)
    override suspend fun tumbmails(id: Long): List<ImagemTumbmail> =dao.tumbmails(id)

}