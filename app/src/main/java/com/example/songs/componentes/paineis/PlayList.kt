package com.example.songs.componentes.paineis

import android.inputmethodservice.Keyboard.Row
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemsListaPlaylists
import com.example.songs.componentes.Miniplayer
import com.example.songs.viewModels.ViewModelListas

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlyList(modifier: Modifier =Modifier,
            windowSizeClass: WindowSizeClass,
            paddingValues: PaddingValues,
            transicaoMiniPlyer:MutableTransitionState<Boolean>,
            vm:ViewModelListas){
     val plylist=vm.plylist.collectAsState()
    Box(modifier = modifier){
        LazyVerticalGrid(columns = GridCells.Fixed(  if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 2 else 3),
                         modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter)
                                            .padding(bottom = if (transicaoMiniPlyer.targetState) 80.dp else 0.dp) ) {

            item {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier=Modifier.clickable {
                        vm.adicionarPlyList("nova")
                    }) {
                    Text("Criar Playlist")
                    IconButton(onClick = {
                        vm.adicionarPlyList("nova1")
                    }) {
                        Icon(Icons.Rounded.AddCircle, contentDescription = null)
                    }
                }
            }

            items(items = plylist.value) {
                ItemsListaPlaylists(modifier=Modifier.combinedClickable(onClick = {}, onLongClick = {
                    vm.excluirPlyList()
                }) ,item = it)
            }


        }


    }

}

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