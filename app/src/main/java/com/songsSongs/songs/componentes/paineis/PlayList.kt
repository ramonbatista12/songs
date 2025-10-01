 package com.songsSongs.songs.componentes.paineis

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.test.espresso.device.sizeclass.WidthSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.componentes.BotoesDeSelelcaoOpcoesDePlyList
import com.songsSongs.songs.componentes.Icones
import com.songsSongs.songs.componentes.ItemsListaPlaylists
import com.songsSongs.songs.viewModels.ViewModelListas

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlyList(modifier: Modifier =Modifier,
            windowSizeClass: WindowSizeClass,
            paddingValues: PaddingValues,
            transicaoMiniPlyer:MutableTransitionState<Boolean>,
            vm:ViewModelListas,
            acaONavegacao:(idPlyList:Long)->Unit={},
            acaoNavegarDialoCriarPlaylist:()->Unit={},
            acaoNavegarOpcoes:(id:Long?)->Unit={},
            acaoNavegarIdAlbum: (String) -> Unit={},
            acaoNavegarIdArtista: (String) -> Unit={}){

    val medicoes=remember { com.songsSongs.songs.componentes.MedicoesItemsDeList() }
    val gradcels=medicoes.gradCell(windowSizeClass)
    val selecionado = remember { mutableStateOf<Icones>(Icones.PlayList) }
    val visivel = Array<MutableState<Boolean>>(3,init={
        if(it==0) remember { mutableStateOf(true) }
        else remember { mutableStateOf(false)} } )

    Box(modifier = modifier){

        BotoesDeSelelcaoOpcoesDePlyList(modifier=Modifier.align(Alignment.TopStart),
                                        seleconado=selecionado,
                                        windowSizeClass=windowSizeClass)
       when (selecionado.value) {

           Icones.PlayList-> {
               LaunchedEffect(Unit) {visivel[0].value=true
                                     visivel[1].value=false
                                     visivel[2].value=false}
               AnimatedVisibility(visible = visivel[0].value, enter = slideInHorizontally(), exit = slideOutHorizontally(),modifier=Modifier.align(androidx.compose.ui.Alignment.Center)) {
               ListaPlyList(m=Modifier.align(androidx.compose.ui.Alignment.Center),
                                          boxScope = this@Box,
                                          vm = vm,
                                          gradcels=gradcels,
                                          windowSizeClass=windowSizeClass,
                                          transicaoMiniPlyer=transicaoMiniPlyer,
                                          acaONavegacao=acaONavegacao,
                                          acaoNavegarDialoCriarPlaylist=acaoNavegarDialoCriarPlaylist,
                                          acaoNavegarOpcoes=acaoNavegarOpcoes)} }

           Icones.Album-> {
               LaunchedEffect(Unit) {visivel[0].value=false
                                     visivel[1].value=true
                                     visivel[2].value=false}
               AnimatedVisibility(visible = visivel[1].value, enter = slideInHorizontally(), exit = slideOutHorizontally(),modifier=Modifier.align(androidx.compose.ui.Alignment.Center)) {
               ListaDeAlbums(modifier=Modifier.align(androidx.compose.ui.Alignment.Center),
                                        windowSizeClass=windowSizeClass,
                                        transicaoMiniPlyer = transicaoMiniPlyer,
                                        vm=vm,acaoNavegarPorId = acaoNavegarIdAlbum )}}

           Icones.Artista-> {
               LaunchedEffect(Unit) {visivel[0].value=false
                                     visivel[1].value=false
                                     visivel[2].value=true}
               AnimatedVisibility(visible = visivel[2].value, enter = slideInHorizontally(), exit = slideOutHorizontally(),modifier=Modifier.align(androidx.compose.ui.Alignment.Center)) {
               ListaDeArtistas(modifier=Modifier.align(androidx.compose.ui.Alignment.Center),
                                            windowSizeClass=windowSizeClass,
                                            transicaoMiniPlyer=transicaoMiniPlyer,
                                            vmodel = vm,
                                            acaoNavegarPorId = acaoNavegarIdArtista)} }

           else->{
               LaunchedEffect(Unit) {Log.d("erro ","erro ao selecionar a playlist")}

           }


    }

}}

@Composable
fun ListaPlyList(m: Modifier=Modifier,boxScope: BoxScope,
                 vm: ViewModelListas,
                 gradcels:Int,
                  windowSizeClass: WindowSizeClass ,
                 transicaoMiniPlyer:MutableTransitionState<Boolean>,
                 acaONavegacao:(idPlyList:Long)->Unit,
                 acaoNavegarDialoCriarPlaylist: () -> Unit,
                 acaoNavegarOpcoes:(id:Long?)->Unit){
    with(boxScope) {
        val plylist = vm.plylist.collectAsState()
        LazyVerticalGrid(
            columns = GridCells.Fixed(gradcels),
            modifier = m.padding(bottom = if (transicaoMiniPlyer.targetState) 80.dp else 0.dp,
                                 start = 10.dp,
                                 end = 10.dp,
                                 top = if(windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) 55.dp
                                       else 65.dp),
        ) {

            item(span = { GridItemSpan(1) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = acaoNavegarDialoCriarPlaylist) {
                        Text("Criar Playlist")
                        Icon(Icons.Rounded.AddCircle, contentDescription = null)
                    }
                }
            }

            items(items = plylist.value) {
                ItemsListaPlaylists(modifier = Modifier.clickable {
                    acaONavegacao(it.id)
                }, vm = vm, item = it, acaoNavegarOpcoes = acaoNavegarOpcoes)
            }
        }

    }}


@Preview
@Composable
fun PreviewPlyList() {
    Surface {
        Scaffold(modifier = Modifier.padding(10.dp).safeContentPadding().safeDrawingPadding().safeGesturesPadding()) {
            val transicaoMiniPlyer = remember { MutableTransitionState(true) }
            Box(modifier = Modifier.padding(paddingValues = it)){
              //  PlyList(windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,paddingValues = it,transicaoMiniPlyer = transicaoMiniPlyer)
            }
        }

    }
}