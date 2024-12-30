package com.example.songs.componentes.paineis

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemsAlbums
import com.example.songs.componentes.ItemsAlbusColuna

/*
* responsavel por esibir a  lista de albums
* */
@Composable
fun ListaDeAlbums(modifier: Modifier = Modifier,windowSizeClass: WindowSizeClass,transicaoMiniPlyer:MutableTransitionState<Boolean>){

  Box(modifier = modifier.fillMaxSize()){

      LazyVerticalGrid(modifier = Modifier.align(Alignment.TopCenter).padding(bottom = if(transicaoMiniPlyer.targetState) 80.dp else 20.dp),
                       columns = GridCells.Fixed(if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 1 else 3),
                       horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
          items(80){
              if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
                  ItemsAlbums(modifier=Modifier.clickable {
                      transicaoMiniPlyer.targetState=!transicaoMiniPlyer.targetState
                  })
              else
                  ItemsAlbusColuna(modifier=Modifier.clickable {
                      transicaoMiniPlyer.targetState=!transicaoMiniPlyer.targetState
                  })

          }

      }


  }
}