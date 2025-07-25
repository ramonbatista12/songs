package com.songsSongs.songs.repositorio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Size

import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.Q)
class RepositorioService(var context: Context?):InterfacePlylist,InterfasseMediaStore {

private var plylist=PlyLists(context)
private var mediaStore=ManipularMediaStore(context)
private var imageLoader:ImagerLoad?=ImagerLoad(context)
   // comunicacao com a clsse responsavel pro gerenciar a media store
    @RequiresApi(Build.VERSION_CODES.Q)

    override fun getMusics()= mediaStore.getMusics()
    override fun getAlbums()=  mediaStore.getAlbums()
    override fun getArtistas()= mediaStore.getArtistas()
    override fun getMusicasPorArtista(id:Long)= mediaStore.getMusicasPorArtista(id)
    override fun getMusicasPorAlbum(id:Long)= mediaStore.getMusicasPorAlbum(id)

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getMetaData(uri: Uri, id: Long):Bitmap?{
   try {
       val resolver = this.context?.contentResolver
       val tumbmail=resolver?.loadThumbnail(uri,Size(100,100),null)
       return tumbmail
   }catch (e:Exception){
       return null
   }

 }


    //comunicao com a classe responsavel pro gerenciar a plylist
    override suspend fun criarPlyList(nome: String) =plylist.criarPlyList(nome)
    override suspend fun criarPlaylist(nome: String, mediaItem: MediaItem)= plylist.criarPlaylist(nome,mediaItem)
    override suspend fun adicionarAplyList(idPlylist: Long, mediaItem: MediaItem) =plylist.adicionarAplyList(idPlylist,mediaItem)
    override suspend fun removerPlaylist(idPlylist: Long)=plylist.removerPlaylist(idPlylist)
    override suspend fun removerItemDaPlaylist(idPlylist: Long) = plylist.removerItemDaPlaylist(idPlylist)
    override suspend fun atualizarPlylist(plylist: ListaPlaylist) = this.plylist.atualizarPlylist(plylist)
    override suspend fun removerItemDaPlyList(idMedia: String) = plylist.removerItemDaPlyList(idMedia)
    override suspend fun tumbmails(id: Long): List<ImagemTumbmail> =plylist.tumbmails(id)
    override fun listaPlaylist(): Flow<List<ListaPlaylist>> =plylist.listaPlaylist()
    override  fun mediaItemsDaPlylist(idPlylist: Long): Flow<List<ItemsDeMedia>> = plylist.mediaItemsDaPlylist(idPlylist)

   //obter imagems
    suspend fun getBitmap(uri: Uri): Bitmap? = imageLoader?.getBitmap(uri)
    suspend fun getBitmap(uri: Uri,whidt:Int,height:Int): Bitmap? = imageLoader?.getBitmap(uri,whidt, height)

   fun finalizar(){
       plylist.finalizar()
       mediaStore.finalizar()
       imageLoader?.finalizar()
   }


}

