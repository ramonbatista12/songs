package com.songsSongs.songs.viewModels

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.songsSongs.songs.repositorio.ListaPlaylist
import com.songsSongs.songs.repositorio.RepositorioService
import com.songsSongs.songs.servicoDemidia.PlyListStados
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class ViewModelListas(val repositorio: RepositorioService, val estado:MutableStateFlow<ResultadosConecaoServiceMedia>):ViewModel(){

   @RequiresApi(Build.VERSION_CODES.Q)
    val _listas=repositorio.getMusics().flowOn(Dispatchers.IO).map {

       delay(2000)
       if(it.isEmpty()){

           ListaMusicas.Vasia}
       else if(it.isNotEmpty()){

           ListaMusicas.Lista(it)
       }

       else {

           ListaMusicas.caregando}

   }
   @RequiresApi(Build.VERSION_CODES.Q)
   private val _albums=repositorio.getAlbums().flowOn(Dispatchers.IO).map {
       if(it.isEmpty()){
           ListaAlbums.Vasia
       }
       else ListaAlbums.Lista(it)
   }
   @RequiresApi(Build.VERSION_CODES.Q)
   private val _artistas=repositorio.getArtistas().flowOn(Dispatchers.IO)
   private val _plylist=repositorio.listaPlaylist()
   private val estadoPlylist=MutableStateFlow<PlyListStados>(PlyListStados.Todas)

   private val scope= viewModelScope
   private var job:Job?=null
   private var jobColetaPlylist:Job?=null
   @RequiresApi(Build.VERSION_CODES.Q)
   val listas=_listas.stateIn(scope=scope,
                              started = SharingStarted.WhileSubscribed(5000),
                              initialValue = ListaMusicas.caregando)
   @RequiresApi(Build.VERSION_CODES.Q)
   val albums=_albums.stateIn(scope=scope,
                              started = SharingStarted.WhileSubscribed(5000),
                              initialValue = ListaAlbums.Vasia)
   @RequiresApi(Build.VERSION_CODES.Q)
   val artistas=_artistas.stateIn(scope=scope,
                                  started = SharingStarted.WhileSubscribed(5000),
                                  initialValue = emptyList())
   val _estadoPlylsist=estadoPlylist.stateIn(scope=scope,
                                             started = SharingStarted.WhileSubscribed(5000),
                                             initialValue = PlyListStados.Todas)
   private val playlistAtual = MutableStateFlow<List<MediaItem>>(emptyList())
    val _playListAtual=playlistAtual.stateIn(scope=scope,
                                             started = SharingStarted.WhileSubscribed(5000),
                                             initialValue = emptyList())
    val plylist=_plylist.stateIn(scope=scope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        scope.launch {
           estado.collect{
               when(it){

                   is ResultadosConecaoServiceMedia.Conectado->{
                       Log.e("estado da plylist","coeltando plylist service conectado chando a coleta")
                       if(job!=null) job?.cancel()
                       job=scope.launch {
                           scope.launch {
                               Log.e("estado da plylist","coeltando plylist")
                               it.setvice.plyListStados.collect{
                                   Log.e("estado da plylist","coeltando plylist valor $it")
                                   estadoPlylist.value=it
                               }
                           }
                           scope.launch {
                               plylist().collect{

                                   playlistAtual.value=it
                               }
                           }

                       }
                   }
                   else->{
                       Log.e("estado da plylist","coeltando plylist desconectado")
                       if(job!=null) job?.cancel()

                   }
               }
           }

        }

    }


    fun flowAulbumId(id:Long)=repositorio.getMusicasPorAlbum(id).flowOn(Dispatchers.IO)
    fun flowArtistaId(id:Long)=repositorio.getMusicasPorArtista(id).flowOn(Dispatchers.IO)
    fun flowPlaylistId(id:Long)=repositorio.mediaItemsDaPlylist(id).flowOn(Dispatchers.IO).map {
        it.map {

            MediaItem.Builder().setMediaId(it.idMedia)
                               .setUri(it.uri).setMediaMetadata(MediaMetadata.Builder().setArtworkUri(Uri.parse(it.uri))
                                                                                       .setTitle(it.titulo)
                                                                                       .setArtist(it.artista)
                                                                                       .setAlbumArtist(it.album)
                                                                                       .setDurationMs(it.duracao.toLong())
                                                                                       .build())
                               .build()
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
     @RequiresApi(Build.VERSION_CODES.Q)
     fun plylist(): Flow<List<MediaItem>> =   when(val r=estadoPlylist.value){

        is PlyListStados.Todas->{repositorio.getMusics()}
        is PlyListStados.Album->{flowAulbumId(r.albumId)}
        is PlyListStados.Playlist->{flowPlaylistId(r.playlistId).flowOn(Dispatchers.IO)}
        is PlyListStados.Artista->{flowArtistaId(r.artistaId)}
        else ->{emptyFlow()}
    }.flowOn(Dispatchers.IO)



     fun mudarPlylist(plyListStado: PlyListStados){
       scope.launch {
           Log.e("service","mudarPlylist")
           when(val e=estado.value){

               is ResultadosConecaoServiceMedia.Conectado->{
                   Log.e("service","conectado mudarPlylist $plylist")
                   e.setvice.muudarPlyList(plyListStado)
               }
               else->{ Log.e("service","desconectado mudarPlylist")}
           }
       }
   }


    fun criarNovaPlylist(nome:String, acaoDecomclusao:()->Unit){
        scope.launch(Dispatchers.IO) {
            repositorio.criarPlyList(nome)
        }.invokeOnCompletion {
            scope.launch(Dispatchers.Main) {
                acaoDecomclusao()
            }

        }
    }

    fun adicionarMusicaNaPlyList(m: MediaItem,idPlylist:Long,acaoDecomclusao: () -> Unit={}){
        scope.launch(Dispatchers.IO) {

            repositorio.adicionarAplyList(idPlylist,m)
        }.invokeOnCompletion {
            scope.launch(Dispatchers.Main) {
                acaoDecomclusao()
            }
        }
    }

    fun adicionarMusicaNaPlyList(idAlbum:Long,idPlylist:Long,acaoDecomclusao: () -> Unit={}){
        scope.launch(Dispatchers.IO) {
          val lista = MutableStateFlow<List<MediaItem>>(emptyList())

            flowAulbumId(idAlbum).collectLatest {
                lista.value=it
            }

            lista.value.forEach {
                adicionarMusicaNaPlyList(it,idPlylist)
            }
        }.invokeOnCompletion {
            scope.launch(Dispatchers.Main) {
        }
    }
    }

    fun excluirPlyList(idDalsita:Long,acaoDecomclusao: () -> Unit){
        scope.launch(Dispatchers.IO) {
           repositorio.removerPlaylist(idDalsita)
        }.invokeOnCompletion {
           scope.launch(Dispatchers.Main) { acaoDecomclusao() }
        }

    }
    fun editarTituloPlyList(id: Long,titulo:String,acaoDecomclusao: () -> Unit){
        scope.launch(Dispatchers.IO) {
            repositorio.atualizarPlylist(ListaPlaylist(id,titulo))
        }.invokeOnCompletion {
            scope.launch(Dispatchers.Main){
                acaoDecomclusao()
            }
        }
    }

    fun removerDaPlyList(idMedia:String,acaoDecomclusao: () -> Unit){
        scope.launch(Dispatchers.IO) {
            repositorio.removerItemDaPlyList(idMedia)
        }.invokeOnCompletion {
            scope.launch(Dispatchers.Main) {
                acaoDecomclusao()
            }
        }
    }

    suspend fun getTumbmail(id:Long)=repositorio.tumbmails(id)

    override fun onCleared() {
        if(job!=null){
            job?.cancel()
            job=null
        }
        scope.cancel()
        super.onCleared()
    }

    suspend fun getImageBitMap(uri: Uri): Bitmap?{
        return scope.async { repositorio.getBitmap(uri) }.await()
    }

}

class FabricaViewModelLista(){
    fun fabricar(r:RepositorioService,estado: MutableStateFlow<ResultadosConecaoServiceMedia>)=object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelListas(r,estado) as T
        }
    }
}