package com.songsSongs.songs.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.songsSongs.songs.repositorio.RepositorioService
import com.songsSongs.songs.servicoDemidia.PlyListStados
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModelPlyList(val estado: MutableStateFlow<ResultadosConecaoServiceMedia>,val repositorio: RepositorioService): ViewModel(){
    private val scop = viewModelScope
    private val plylist=MutableStateFlow<List<MediaItem>>(emptyList())
    val _plylist=plylist.stateIn(scop,started = SharingStarted.WhileSubscribed(5000),initialValue = emptyList())
    private val estadoPlylist=MutableStateFlow<PlyListStados>(PlyListStados.Todas)
    var job:Job?=null
    init {
        scop.launch {
        scop.launch {
        estado.collect{

        when(it){
            is ResultadosConecaoServiceMedia.Conectado-> {
                scop.launch {
                    it.setvice.plyListStados.collect{
                        estadoPlylist.value=it
                    }
                }
                scop.launch(Dispatchers.IO) {
                    estadoPlylist.collect {
                    when(it){
                        is PlyListStados.Album->{
                            if(job!=null) job?.cancel()
                            job=scop.launch {
                                repositorio.getMusicasPorAlbum(it.albumId).collect{
                                    plylist.value=it
                                }
                            }
                        }
                        is PlyListStados.Artista->{
                            if(job!=null) job?.cancel()
                            job=scop.launch {
                                repositorio.getMusicasPorArtista(it.artistaId).collect{
                                    plylist.value=it
                                }
                            }
                        }
                        is PlyListStados.Todas->{
                            if(job!=null) job?.cancel()
                            job=scop.launch {
                                repositorio.getMusics().collect{
                                    plylist.value=it
                                }
                            }
                        }
                        is PlyListStados.Playlist-> {
                            Log.d("ViewModelPlyList", "ViewModelPlyList: ${it.playlistId}")
                            if(job!=null) job?.cancel()
                            job=scop.launch {
                                repositorio.mediaItemsDaPlylist(it.playlistId).catch {
                                    Log.d("ViewModelPlyList", "ViewModelPlyList: ${it.message}")
                                }.collect{
                                    val lista=it.map {
                                        MediaItem.Builder().setMediaId(it.idMedia)
                                            .setUri(it.uri).setMediaMetadata(
                                                MediaMetadata.Builder().setArtworkUri(Uri.parse(it.uri))
                                                .setTitle(it.titulo)
                                                .setArtist(it.artista)
                                                .setAlbumArtist(it.album)
                                                .setDurationMs(it.duracao.toLong())
                                                .build())
                                            .build()
                                    }
                                    plylist.value=lista
                                }
                            }
                        }
                    }}
                }
            }
            else->{if(job!=null) job?.cancel()}
        } }}
        }
    }

    override fun onCleared() {
        super.onCleared()
        if(job!=null) job?.cancel()
        job=null

    }
}

class FabricaViewModelPlylist(){
    fun fabricar(estado: MutableStateFlow<ResultadosConecaoServiceMedia>,repositorio: RepositorioService)=object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelPlyList(estado,repositorio) as T
        }

    }
}