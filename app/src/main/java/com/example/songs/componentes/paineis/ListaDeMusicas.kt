package com.example.songs.componentes.paineis

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import com.example.songs.componentes.Miniplayer

@Composable
fun ListaDemusicas(modifier: Modifier = Modifier,paddingValues: PaddingValues,windowSizeClass: WindowSizeClass){

    val transicao = remember { MutableTransitionState(true) }
    val texto = remember { mutableStateOf("Nome da musica no mine plyer") }
    Box(modifier = modifier.fillMaxSize()){

      LazyVerticalGrid(columns = GridCells.Fixed(if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 1 else 3),horizontalArrangement =Arrangement.SpaceBetween ,modifier = Modifier.align(
            Alignment.TopCenter).padding( bottom = if(transicao.targetState) 80.dp else 20.dp ).wrapContentSize()) {
            items(80){
                if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT||windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
                    ItemDaLista(modifier = Modifier.clickable(onClick = {
                        transicao.targetState=!transicao.targetState
                        texto.value="Nome da musica no mine plyer $it"

                    }))
                }else{
                    ItemsListaColunas(modifier=Modifier.clickable(onClick = {transicao.targetState=!transicao.targetState}))
                }
            }


        }

        AnimatedVisibility(visible =transicao.targetState,modifier = Modifier.align(Alignment.BottomCenter)){
            Miniplayer(modifier = Modifier.align(Alignment.BottomCenter), text = texto.value)
        }







    }






}

@Preview(showBackground = true

)
@Composable
fun previewListaDemusicas(){

//Surface {
        Scaffold(topBar = { BarraSuperio(titulo = "Todas As Musicas")},modifier = Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding() ){
            ListaDemusicas(paddingValues = it, windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass)
        }

       // }

    }