package com.songsSongs.songs.componentes.paineis

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.componentes.Banner
import com.songsSongs.songs.componentes.ItemsArtistas
import com.songsSongs.songs.viewModels.ViewModelListas

@Composable
fun ListaDeArtistas(modifier: Modifier = Modifier,
                    windowSizeClass: WindowSizeClass,
                    transicaoMiniPlyer: MutableTransitionState<Boolean>,
                    vmodel: ViewModelListas,
                    acaoNavegarPorId:(String)->Unit){

    val listaDeArtistas=vmodel.artistas.collectAsState()
    val medicoes =remember { com.songsSongs.songs.componentes.MedicoesItemsDeList() }
    Box(modifier = modifier.fillMaxSize()
                           .padding(top =  if(windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) 55.dp
                                           else 65.dp)){

        LazyVerticalGrid(modifier = Modifier.align(Alignment.TopCenter).padding(bottom = if(transicaoMiniPlyer.targetState) 80.dp else 0.dp),
            columns = GridCells.Fixed(medicoes.gradCell(windowSizeClass)),
            horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            itemsIndexed(items = listaDeArtistas.value){indice,item->
                if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
                    ItemsArtistas(modifier= Modifier.clickable {
                        acaoNavegarPorId("${item.idDoArtista}")
                    },item)
                else
                    ItemsArtistas(modifier= Modifier.clickable {
                        acaoNavegarPorId("${item.idDoArtista}")
                    },item)

            }

        }



    }
}