package com.example.songs.componentes.paineis

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import com.example.songs.repositorio.PlyList
import com.example.songs.servicoDemidia.PlyListStados
import com.example.songs.viewModels.ViewModelListas

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlayListId(modifier: Modifier = Modifier,
            paddingValues: PaddingValues,
            windowSizeClass: WindowSizeClass,
            transicaoMiniPlyer: MutableTransitionState<Boolean>,
            vm: ViewModelListas,
            acaoCarregarPlyer:(List<MediaItem>, indice:Int)->Unit, id:Long,
            acaoNavegarOpcoes:(item: MediaItem?)->Unit={} ){
    val lista=vm.flowPlaylistId(id).collectAsState(initial = emptyList())

    val texto = remember { mutableStateOf("Nome da musica no mine plyer") }
    Box(modifier = modifier.fillMaxSize()){
        val gradcels:(w: WindowSizeClass)->Int ={ w->
            if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 1
            else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM)
                if(w.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 2
                else 1
            else if (windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.EXPANDED)
                if (w.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 2
                else 3
            else   3

        }
        LazyVerticalGrid(columns = GridCells.Fixed(gradcels(windowSizeClass)),
            horizontalArrangement = Arrangement.SpaceBetween ,
            modifier = Modifier.align(
                Alignment.TopCenter).padding( bottom = if(transicaoMiniPlyer.targetState) 70.dp else 20.dp ).wrapContentSize()) {

            itemsIndexed(items= lista.value){ indice, item->
                if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT||windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM){
                    ItemDaLista(modifier = Modifier.clickable(onClick = {
                        acaoCarregarPlyer(lista.value,indice)
                        vm.mudarPlylist(PlyListStados.Playlist(id))

                    }), item = item,acaoNavegarOpcoes = acaoNavegarOpcoes)
                }else{
                    ItemsListaColunas(modifier= Modifier.clickable(onClick = {acaoCarregarPlyer(lista.value,indice)}), item = item)
                }
            }




        }




    }









}