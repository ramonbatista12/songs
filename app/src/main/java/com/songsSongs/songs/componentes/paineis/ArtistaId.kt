package com.songsSongs.songs.componentes.paineis

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
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.componentes.ItemDaLista
import com.songsSongs.songs.servicoDemidia.PlyListStados
import com.songsSongs.songs.viewModels.ViewModelListas

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ArtistaId(modifier: Modifier = Modifier,
            paddingValues: PaddingValues,
            windowSizeClass: WindowSizeClass,
            transicaoMiniPlyer: MutableTransitionState<Boolean>,
            viewModelListas: ViewModelListas,
            acaoCarregarPlyer:(List<MediaItem>, indice:Int)->Unit,
            id:Long,acaoNavegarOpcoes:(item:MediaItem?)->Unit={} ){
    val lista=viewModelListas.flowArtistaId(id).collectAsState(emptyList())
    val texto = remember { mutableStateOf("Nome da musica no mine plyer") }
    val medicoes=remember { com.songsSongs.songs.componentes.MedicoesItemsDeList() }
    Box(modifier = modifier.fillMaxSize()){
        val gradcels=medicoes.gradCell(windowSizeClass)
        LazyVerticalGrid(columns = GridCells.Fixed(gradcels),horizontalArrangement = Arrangement.SpaceBetween ,modifier = Modifier.align(
            Alignment.TopCenter).padding( bottom = if(transicaoMiniPlyer.targetState) 70.dp else 20.dp ).wrapContentSize()) {



                    itemsIndexed(items=lista.value){ indice,item->
                        if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT||windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM){
                            ItemDaLista(modifier = Modifier.clickable(onClick = {
                                acaoCarregarPlyer(lista.value,indice)
                                viewModelListas.mudarPlylist(PlyListStados.Artista(id))

                            }),
                                item = item, vm = viewModelListas ,acaoNavegarOpcoes = acaoNavegarOpcoes)
                        }else{
                            ItemDaLista(modifier= Modifier.clickable(onClick = {acaoCarregarPlyer(lista.value,indice)}), vm = viewModelListas ,item = item)
                        }
                    }







        }









    }






}