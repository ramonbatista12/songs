package com.songsSongs.songs.componentes.paineis

import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.componentes.Banner
import com.songsSongs.songs.componentes.ItemsAlbums
import com.songsSongs.songs.componentes.LoadingAlbumsColuna
import com.songsSongs.songs.componentes.LoadingListaAlbums
import com.songsSongs.songs.componentes.MedicoesItemsDeList
import com.songsSongs.songs.viewModels.ListaAlbums
import com.songsSongs.songs.viewModels.ViewModelListas

/*
* responsavel por esibir a  lista de albums
* */
@Composable
fun ListaDeAlbums(modifier: Modifier = Modifier
                  ,windowSizeClass: WindowSizeClass,
                  transicaoMiniPlyer:MutableTransitionState<Boolean>,
                  vm:ViewModelListas,acaoNavegarPorId:(s:String)->Unit){
  val listaAlbun =vm.albums.collectAsState(initial = ListaAlbums.caregando)
  Box(modifier = modifier.fillMaxSize()){
      val medicoes=remember { MedicoesItemsDeList() }
      LazyVerticalGrid(modifier = Modifier.align(Alignment.TopCenter).padding(bottom = if(transicaoMiniPlyer.targetState) 80.dp else 45.dp),
                       columns = GridCells.Fixed(medicoes.gradCell(windowSizeClass)),
                       horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
          when(val a=listaAlbun.value){


          is ListaAlbums.Lista->{
          items(items = a.lista){
              if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
                  ItemsAlbums(modifier=Modifier.clickable {
                      Log.d("id album", "ListaDeAlbums: ${it.idDoalbum}")
                      acaoNavegarPorId(it.idDoalbum.toString())
                  },it)
              else
                  ItemsAlbums(modifier=Modifier.clickable {
                      Log.d("id album", "ListaDeAlbums: ${it.idDoalbum}")
                      acaoNavegarPorId(it.idDoalbum.toString())
                  },it)

          }
      }
          is ListaAlbums.Vasia->{}
          is ListaAlbums.caregando->{
              items(5){
                  if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
                      LoadingAlbumsColuna()
                  else
                      LoadingListaAlbums()

              }
          }
          else->{}
      }

      }
if(!transicaoMiniPlyer.targetState)
    Row (modifier = Modifier.align(Alignment.BottomCenter)){
        Banner() }

  }
}