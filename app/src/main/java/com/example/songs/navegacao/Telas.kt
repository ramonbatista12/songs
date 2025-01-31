package com.example.songs.navegacao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.songs.application.AplicationCuston
import com.example.songs.componentes.ItemsAlbums
import com.example.songs.componentes.ItemsAlbusColuna
import com.example.songs.componentes.paineis.AlbumId
import com.example.songs.componentes.paineis.ArtistaId
import com.example.songs.componentes.paineis.BigPlayer
import com.example.songs.componentes.paineis.ListaDeAlbums
import com.example.songs.componentes.paineis.ListaDeArtistas
import com.example.songs.componentes.paineis.ListaDemusicas
import com.example.songs.componentes.paineis.PlyList
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.viewModels.FabricaViewModelLista
import com.example.songs.viewModels.ViewModelListas
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
* Navgrafic e o grafico de navegacao em sim suas rotas sao determinadas pela classe DestinosDENavegacao
* rotas disponiveis  :Todas,Playlist,Album,Configuracoes,Player
* cada funcoes e responsavel por adiministra suas funcoes como reproduzir ou listar as musicas
* mas cada funcao  e responsavel por checar a o tamanho da classe de janela
* e determinar camo deve seer esibido seus dados em alguns casos
* e determinado um painel separado para diferentes tamanhos de tela
*
* */


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navgrafic(navController: NavHostController,
              windowSizeClass: WindowSizeClass,
              modifier: Modifier,
              paddingValues: PaddingValues,
              transicaoMiniPlyer:MutableTransitionState<Boolean>,
              vm: VmodelPlayer,
              acaoCaregarPlyer:(List<MediaItem>,indice:Int)->Unit,
              acaoAvisoBigplyer:()->Unit,
              estadoService:MutableStateFlow<ResultadosConecaoServiceMedia>){
    val vmLista:ViewModelListas=viewModel(factory = FabricaViewModelLista().fabricar(r= AplicationCuston.conteiner.repositorio,estadoService))
    val scope=rememberCoroutineScope()
NavHost(navController = navController, startDestination = DestinosDENavegacao.DestinosDeTela.Todas,modifier=modifier){
  composable<DestinosDENavegacao.DestinosDeTela.Todas>{
   Box{
       ListaDemusicas(modifier = Modifier,
                      windowSizeClass = windowSizeClass,
                      paddingValues = paddingValues,
                      transicaoMiniPlyer = transicaoMiniPlyer,
                      viewModelListas = vmLista,
                      acaoCarregarPlyer = acaoCaregarPlyer)
   }
  }

  composable<DestinosDENavegacao.DestinosDeTela.Playlist>{
      Box{
        PlyList(modifier = modifier,windowSizeClass = windowSizeClass,paddingValues = paddingValues,transicaoMiniPlyer = transicaoMiniPlyer, vm = vmLista)
       }}


  composable<DestinosDENavegacao.DestinosDeTela.Album>{
      Box(modifier=Modifier){
          ListaDeAlbums(windowSizeClass = windowSizeClass,
                       transicaoMiniPlyer = transicaoMiniPlyer,
                       vm =vmLista,
                       acaoNavegarPorId = {s->
                           Log.d("id artista", "Navgrafic: $s")
                       scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.AlbumId(s.toLong()))}})
         }}


  composable<DestinosDENavegacao.DestinosDeTela.Configuracoes>{}

  //
  composable<DestinosDENavegacao.DestinosDeTela.Player>{
      BigPlayer(windowSizeClass = windowSizeClass,
                paddingValues = paddingValues,
                vm = vm,acaoAvisoBigplyer = acaoAvisoBigplyer,
          vmlista =viewModel(factory = FabricaViewModelLista().fabricar(AplicationCuston.repositorio,estadoService)) )

  }

  composable<DestinosDENavegacao.DestinosDeTela.Artista>{
      ListaDeArtistas(windowSizeClass = windowSizeClass,
                      transicaoMiniPlyer = transicaoMiniPlyer,
                      acaoNavegarPorId ={s->
                          Log.d("id artista", "Navgrafic: $s")
                      scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.ArtistaId(s.toLong()))
                                     }},
                      vmodel = vmLista)

  }

  composable<DestinosDENavegacao.DestinosDeTela.ArtistaId>{

      val artistaId:DestinosDENavegacao.DestinosDeTela.ArtistaId=it.toRoute()
      ArtistaId(modifier = Modifier,
          windowSizeClass = windowSizeClass,
          paddingValues = paddingValues,
          transicaoMiniPlyer = transicaoMiniPlyer,
          viewModelListas = vmLista,
          acaoCarregarPlyer = acaoCaregarPlyer,id=artistaId.id)
  }

   composable<DestinosDENavegacao.DestinosDeTela.AlbumId>{
       val id:DestinosDENavegacao.DestinosDeTela.AlbumId=it.toRoute()
       AlbumId(
           modifier = Modifier,
           windowSizeClass = windowSizeClass,
           paddingValues = paddingValues,
           transicaoMiniPlyer = transicaoMiniPlyer,
           viewModelListas = vmLista,
           acaoCarregarPlyer = acaoCaregarPlyer,id=id.id
       )

   }


}


}