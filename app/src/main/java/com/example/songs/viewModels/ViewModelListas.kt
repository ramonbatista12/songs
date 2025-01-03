package com.example.songs.viewModels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.songs.repositorio.RepositorioService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ViewModelListas(val repositorio: RepositorioService):ViewModel(){

   @RequiresApi(Build.VERSION_CODES.Q)
    val _listas=repositorio.getMusics().map {
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
   private val _albums=repositorio.getAlbums()
   @RequiresApi(Build.VERSION_CODES.Q)
   private val _artistas=repositorio.getArtistas()
   private val scope= viewModelScope
   @RequiresApi(Build.VERSION_CODES.Q)
   val listas=_listas.stateIn(scope=scope, started = SharingStarted.WhileSubscribed(5000),initialValue = ListaMusicas.caregando)
   @RequiresApi(Build.VERSION_CODES.Q)
   val albums=_albums.stateIn(scope=scope, started = SharingStarted.WhileSubscribed(5000),initialValue = emptyList())
   @RequiresApi(Build.VERSION_CODES.Q)
   val artistas=_artistas.stateIn(scope=scope, started = SharingStarted.WhileSubscribed(5000),initialValue = emptyList())

}

class FabricaViewModelLista(){
    fun fabricar(r:RepositorioService)=object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelListas(r) as T
        }
    }
}