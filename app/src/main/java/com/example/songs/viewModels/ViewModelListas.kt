package com.example.songs.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.songs.repositorio.RepositorioService
import com.example.songs.servicoDemidia.PlyListStados
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class ViewModelListas(val repositorio: RepositorioService, val estado:MutableStateFlow<ResultadosConecaoServiceMedia>):ViewModel(){

   @RequiresApi(Build.VERSION_CODES.Q)
    val _listas=repositorio.getMusics().map {

       delay(2000)
       if(it.isEmpty()){
           Log.d("TAG vm", "listas: ${it.size}")
           ListaMusicas.Vasia}
       else if(it.isNotEmpty()){
           Log.d("TAG vm", "listas: ${it.size}")
           ListaMusicas.Lista(it)
       }

       else {Log.d("TAG vm", "caregando")

           ListaMusicas.caregando}

   }
   @RequiresApi(Build.VERSION_CODES.Q)
   private val _albums=repositorio.getAlbums().map {
       if(it.isEmpty()){
           ListaAlbums.Vasia
       }
       else ListaAlbums.Lista(it)
   }
   @RequiresApi(Build.VERSION_CODES.Q)
   private val _artistas=repositorio.getArtistas()
   private val estadoPlylist=MutableStateFlow<PlyListStados>(PlyListStados.Todas)
   private val scope= viewModelScope
   private var job:Job?=null
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

    init {
        scope.launch {
            when(val r=estado.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    if(job!=null) job?.cancel()
                    job=scope.launch {
                        scope.launch {
                            r.setvice.plyListStados.collect{
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
                    if(job!=null) job?.cancel()

                }
            }
        }

    }
     @RequiresApi(Build.VERSION_CODES.Q)
      fun plylist(): Flow<List<MediaItem>> = estadoPlylist.flatMapConcat {
          val f= repositorio.getPlylist(it)
         f
     }
     fun mudarPlylist(plyListStado: PlyListStados){
       scope.launch {
           when(val e=estado.value){

               is ResultadosConecaoServiceMedia.Conectado->{
                   Log.d("vmLista","mudarPlylist: $plyListStado")
                   e.setvice.muudarPlyList(plyListStado)
               }
               else->{}
           }
       }
   }

    override fun onCleared() {
        if(job!=null){
            job?.cancel()
            job=null
        }
        scope.cancel()
        super.onCleared()
    }

}

class FabricaViewModelLista(){
    fun fabricar(r:RepositorioService,estado: MutableStateFlow<ResultadosConecaoServiceMedia>)=object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelListas(r,estado) as T
        }
    }
}