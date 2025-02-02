package com.example.songs.repositorio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.songs.servicoDemidia.PlyListStados
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@RequiresApi(Build.VERSION_CODES.Q)
class RepositorioService(val context: Context):InterfacePlylist,InterfasseMediaStore {

private val plylist=PlyLists(context)
private val mediaStore=ManipularMediaStore(context)
   // comunicacao com a clsse responsavel pro gerenciar a media store
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(UnstableApi::class)
    override fun getMusics()= mediaStore.getMusics()
    override fun getAlbums()=  mediaStore.getAlbums()
    override fun getArtistas()= mediaStore.getArtistas()
    override fun getMusicasPorArtista(id:Long)= mediaStore.getMusicasPorArtista(id)
    @OptIn(UnstableApi::class)
    override fun getMusicasPorAlbum(id:Long)= mediaStore.getMusicasPorAlbum(id)

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getMetaData(uri: Uri, id: Long):Bitmap?{
   try {
       val resolver = this.context.contentResolver
       val tumbmail=resolver.loadThumbnail(uri,Size(100,100),null)
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
    override fun listaPlaylist(): Flow<List<ListaPlaylist>> =plylist.listaPlaylist()
    override  fun mediaItemsDaPlylist(idPlylist: Long): Flow<List<ItemsDeMedia>> = plylist.mediaItemsDaPlylist(idPlylist)

   // fun getPlylist(estado:PlyListStados): Flow<List<MediaItem>>


}

