package com.example.songs.navegacao

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import java.lang.reflect.Modifier

@Composable
fun Navgrafic(navController: NavHostController,windowSizeClass: WindowSizeClass,paddingValues: PaddingValues){
NavHost(navController = navController, startDestination = DestinosDENavegacao.Todas.rota){
  composable(route = DestinosDENavegacao.Todas.rota){
    Box(modifier = androidx.compose.ui.Modifier){
       LazyVerticalGrid (columns = GridCells.Fixed(if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)1 else 3 )) {
         if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) items(40){
             ItemDaLista()
         }
         else   items(40){
             ItemsListaColunas()
            }


       }

    }


  }
  composable(route = DestinosDENavegacao.Playlist.rota){}
  composable(route = DestinosDENavegacao.Album.rota){}
  composable(route = DestinosDENavegacao.Configuracoes.rota){}
  composable(route = DestinosDENavegacao.Player.rota){}


}


}