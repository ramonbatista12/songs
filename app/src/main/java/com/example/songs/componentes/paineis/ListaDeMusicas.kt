package com.example.songs.componentes.paineis

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.window.core.layout.WindowHeightSizeClass

import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.application.AplicationCuston
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import com.example.songs.componentes.LoadingListaMusicas
import com.example.songs.componentes.LoadingListaMusicasColunas
import com.example.songs.componentes.Miniplayer
import com.example.songs.servicoDemidia.PlyListStados
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.viewModels.FabricaViewModelLista
import com.example.songs.viewModels.ListaMusicas
import com.example.songs.viewModels.ViewModelListas
import kotlinx.coroutines.flow.MutableStateFlow

/*
* responsavel pro esibir a lista de musicas em si
* */
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ListaDemusicas(modifier: Modifier = Modifier,
                   paddingValues: PaddingValues,
                   windowSizeClass: WindowSizeClass,
                   transicaoMiniPlyer:MutableTransitionState<Boolean>,
                   viewModelListas: ViewModelListas,
                   acaoCarregarPlyer:(List<MediaItem>, indice:Int)->Unit,
                   acaoNavegarOpcoes:(MediaItem?)->Unit){
    val lista=viewModelListas.listas.collectAsState(ListaMusicas.caregando)

    val texto = remember { mutableStateOf("Nome da musica no mine plyer") }
    val medicoes=remember { com.example.songs.componentes.MedicoesItemsDeList() }
    Box(modifier = modifier.fillMaxSize()){
      val gradcels=medicoes.gradCell(windowSizeClass)
      LazyVerticalGrid(columns = GridCells.Fixed(gradcels),horizontalArrangement =Arrangement.SpaceBetween ,modifier = Modifier.align(
            Alignment.TopCenter).padding( bottom = if(transicaoMiniPlyer.targetState) 70.dp else 20.dp ).wrapContentSize()) {
           when(val r =lista.value){

               is ListaMusicas.Lista->{
                   itemsIndexed(items=r.lista){ indice,item->
                   if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT||windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
                       ItemDaLista(modifier = Modifier.clickable(onClick = {
                          acaoCarregarPlyer(r.lista,indice)
                          viewModelListas.mudarPlylist(PlyListStados.Todas,)

                       }), item = item,acaoNavegarOpcoes = acaoNavegarOpcoes)
                   }else{
                       ItemsListaColunas(modifier=Modifier.clickable(onClick = {acaoCarregarPlyer(r.lista,indice)}), item = item, acaoNavegarDialogoDeOpcoes = acaoNavegarOpcoes)
                   }
               }
               }
               is ListaMusicas.Vasia->{}
               is ListaMusicas.caregando->{
                   items(5){
                   if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT||windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){

                       LoadingListaMusicas()


                   }else{
                      LoadingListaMusicasColunas()
                   }
                   }
               }

           }




        }









    }






}


@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true

)
@Composable
fun previewListaDemusicas(){

//Surface {
    val transicaoMiniPlyer = remember { MutableTransitionState(true) }
    val localContext=LocalContext.current

        Scaffold(topBar = { BarraSuperio(titulo = "Todas As Musicas")},modifier = Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding() ){
            ListaDemusicas(paddingValues = it,
                          windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
                          transicaoMiniPlyer = transicaoMiniPlyer,
                          viewModelListas = viewModel(factory = FabricaViewModelLista().fabricar(r= AplicationCuston.conteiner.repositorio,
                              MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)
                          )), acaoCarregarPlyer={ it, id->}, acaoNavegarOpcoes = {})
        }

       // }

    }