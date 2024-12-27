package com.example.songs.navegacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.example.songs.componentes.ItemsAlbums
import com.example.songs.componentes.ItemsAlbusColuna
import com.example.songs.componentes.paineis.ListaDeAlbums
import com.example.songs.componentes.paineis.ListaDemusicas

@Composable
fun Navgrafic(navController: NavHostController,windowSizeClass: WindowSizeClass,modifier: Modifier,paddingValues: PaddingValues){
NavHost(navController = navController, startDestination = DestinosDENavegacao.Album.rota,modifier=modifier){
  composable(route = DestinosDENavegacao.Todas.rota){
   Box{
       ListaDemusicas(Modifier.padding(paddingValues),windowSizeClass = windowSizeClass, paddingValues = paddingValues)
   }



  }
  composable(route = DestinosDENavegacao.Playlist.rota){
      Box(modifier=Modifier){


      }

  }
  composable(route = DestinosDENavegacao.Album.rota){
      Box(modifier=Modifier){
          ListaDeAlbums(windowSizeClass = windowSizeClass)


          }
      }

  composable(route = DestinosDENavegacao.Configuracoes.rota){}
  composable(route = DestinosDENavegacao.Player.rota){}


}


}