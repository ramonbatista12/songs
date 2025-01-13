package com.example.songs.componentes.paineis

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.Miniplayer
import com.example.songs.componentes.MiniplayerParaTransicao
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme
import kotlinx.coroutines.launch

/*
* BigPlyer representa o player em si aonde se pode ver os dados da musica em reproducao no momento
* e responsavel por medir e determinar como deve ser esibido em cado tamanho de tela
* */

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BigPlayer(modifier: Modifier = Modifier,windowSizeClass: WindowSizeClass,paddingValues: PaddingValues){
    val  navegacao = rememberListDetailPaneScaffoldNavigator<Long>( )
  if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
      PlayerCompat2(modifier=Modifier)

 else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) PlayerCompat2()
    else  PlyerEspandido(Modifier.padding(paddingValues=paddingValues),windowSizeClass)





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Player(modifier: Modifier=Modifier){
    Column(modifier =modifier.padding(10.dp).background(color = MaterialTheme.colorScheme.background)) {

        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),modifier = Modifier.size(400.dp).clip(
            RoundedCornerShape(15.dp)
        ).border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)), contentDescription = null, tint = DarkPink)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier) {

            Text(text = "Nome da Musica")
            Spacer(Modifier.padding( 8.dp))
            Text(text = "Nome do Artista")
            Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.width(400.dp)) {
            var range= remember {  mutableStateOf<Float>(0f)}
             Slider(value = range.value, onValueChange = {
                 range.value=it
             },colors = SliderDefaults.colors(activeTrackColor = DarkPink), valueRange = 0f..1f)
            Spacer(Modifier.padding(10.dp))

            Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null, tint = DarkPink)

            }
        }
        }


    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlyerParaTransicao(modifier: Modifier=Modifier,sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope: AnimatedVisibilityScope){
    var range= remember {  mutableStateOf<Float>(0f)}
    val scop=rememberCoroutineScope()
    LaunchedEffect(Unit){
        scop.launch {
            while (true){
                range.value+=0.010f
                if(range.value>1f)range.value=0f
                kotlinx.coroutines.delay(1000)
            }
        }
    }
    val stateRange = remember { derivedStateOf { range.value } }
    with(sharedTransitionScope){
        Column(modifier =modifier.padding(10.dp).background(color = MaterialTheme.colorScheme.background)) {

            Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),contentDescription = null,
                                           modifier = Modifier.size(400.dp)
                                                              .clip( RoundedCornerShape(15.dp))
                                                              .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                                                              .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope),)

            Spacer(Modifier.padding(10.dp))
            Column(modifier = Modifier) {

                Text(text = "Nome da Musica",modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope))
                Spacer(Modifier.padding( 8.dp))
                Text(text = "Nome do Artista",modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))
                Spacer(Modifier.padding(10.dp))
                Column(modifier = Modifier.width(400.dp)) {
                    val rangeValue:()->Float ={ stateRange.value }
                    Slider(value = rangeValue(), onValueChange = {
                        range.value=it
                    },colors = SliderDefaults.colors(activeTrackColor = DarkPink), valueRange = 0f..1f)
                    Spacer(Modifier.padding(10.dp))

                    Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                        Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = DarkPink)
                        Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = DarkPink)
                        Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                                       contentDescription = null,
                                                       tint = DarkPink,
                                                       modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope))
                        Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = DarkPink)
                        Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                                       contentDescription = null,
                                                       tint = DarkPink,modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope))

                    }
                }
            }


        }
    }
}


@Composable
fun PlyerComtrasicaoLayt(modifier: Modifier=Modifier,){}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerCompat(modifier: Modifier=Modifier,sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope:AnimatedVisibilityScope,onclick:()->Unit={} ){
    val listaAvberta=remember{ mutableStateOf(false)}
      with(sharedTransitionScope){
          Box(modifier = modifier.fillMaxSize().imePadding().sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),animatedVisibilityScope,
             resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)){
              PlyerParaTransicao(Modifier.align( Alignment.TopCenter),animatedVisibilityScope = animatedVisibilityScope,sharedTransitionScope = sharedTransitionScope)


              IconButton({
                  listaAvberta.value=!listaAvberta.value
                  onclick()
              },modifier = Modifier.align(Alignment.BottomCenter).size(70.dp).padding(5.dp)) {
                  Icon(painter = painterResource(id = R.drawable.baseline_list_24), contentDescription = null, tint = DarkPink, modifier = Modifier.align(
                      Alignment.BottomCenter).size(50.dp).padding(5.dp))

              }


          }
      }


}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerCompat2(modifier: Modifier=Modifier) {
    val listaAvberta = remember { mutableStateOf(false) }
    val slidervalue = remember { mutableStateOf(0f) }

    Box(modifier = Modifier.fillMaxSize()) {

        SharedTransitionLayout {
            AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->

                Box(modifier = modifier.fillMaxSize().imePadding()) {
                    SharedTransitionLayout {
                        AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->
                            if (!targetState) {
                                PlayerCompat(sharedTransitionScope = this@SharedTransitionLayout, animatedVisibilityScope = this@AnimatedContent,onclick = {listaAvberta.value=!listaAvberta.value})

                            } else {
                                Column(Modifier.sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),this@AnimatedContent, resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)) {
                                    MiniplayerParaTransicao(sharedTransitionScope = this@SharedTransitionLayout, animatedVisibilityScope = this@AnimatedContent)
                                    Spacer(Modifier.padding(10.dp))
                                    Row(Modifier.fillMaxWidth(),horizontalArrangement =Arrangement.Center){
                                        IconButton(onClick = {
                                        listaAvberta.value=!listaAvberta.value
                                    },modifier = Modifier.size(60.dp)) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = DarkPink,modifier=Modifier.size(50.dp))
                                    } }
                                    LazyColumn {
                                        items(100) {
                                            ItemDaLista(item = null)
                                        }
                                    }
                                }
                            }


                        }

                    }


                }

            }
        }
    }
}


sealed class ComponetesCompartilhados(val label:String){
    object PlayeBtn:ComponetesCompartilhados(label = "PlayeBtn")
    object NextPlyer:ComponetesCompartilhados(label = "NextPlyer")
    object ImagemEIcones:ComponetesCompartilhados(label = "Imagem/Icones")
    object NomeDaMusica:ComponetesCompartilhados(label = "NomeDaMusica")
    object NomeDoArtista:ComponetesCompartilhados(label = "NomeDoArtista")

}

sealed class LayoutsCompartilhados(val label:String){
    object LayoutPluer:LayoutsCompartilhados(label = "LayoutPluer")
}








    @Composable
fun PlyerEspandido(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass) {
        val progresso = remember { mutableStateOf(0f) }
        Row(modifier.fillMaxSize().padding(bottom = 10.dp), verticalAlignment = Alignment.Top) {
            Box(Modifier.fillMaxWidth(0.5f).clip(RoundedCornerShape(15.dp)).padding(10.dp)) {

                Column(Modifier.align(Alignment.TopCenter)) {
                    val iconeSize =
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 90.dp else 400.dp
                    Icon(
                        painter = painterResource(R.drawable.baseline_music_note_24),
                        contentDescription = null,
                        modifier = Modifier.size(iconeSize).clip(
                            RoundedCornerShape(15.dp)
                        )
                            .border(
                                width = 0.5.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(15.dp)
                            ).align(Alignment.CenterHorizontally), tint = Color.Black
                    )

                    Column {

                        Text(text = "Nome da Musica")
                        Spacer(Modifier.padding(3.dp))
                        Text(text = "Nome do Artista")
                        Spacer(Modifier.padding(3.dp))
                        Column {
                            Slider(
                                value = progresso.value,
                                onValueChange = { progresso.value = it },
                                colors = SliderDefaults.colors(activeTrackColor = DarkPink),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.padding(3.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.size(60.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_shuffle_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.size(60.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.size(60.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_repeat_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.size(60.dp)
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.size(60.dp)
                                )

                            }
                        }


                    }

                }


            }
            Spacer(Modifier.padding(10.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                    .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Playlist", fontFamily = FontFamily.Monospace)
                Box {
                    LazyColumn(Modifier.align(Alignment.TopCenter).fillMaxWidth(1f)) {
                        items(80) {
                            ItemDaLista(item = null)
                        }

                    }


                }
            }


        }


    }






@Preview(showBackground = true)
@Composable
fun PlayerPreview(){
    SongsTheme {
        Surface {
        Scaffold(topBar = { BarraSuperio(titulo = "Plyer") }, modifier = Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding()) {

            val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
            BigPlayer(modifier = Modifier.padding(it),windowSizeClass = windowsizeclass,paddingValues = it)

        }
        }
    }

    }


@Preview(showBackground = true)
@Composable
fun PreviaPlyer2(){
    SongsTheme {
        Surface {
            Scaffold(Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding()) {
             Box(modifier = Modifier.padding(it))  {
              PlayerCompat2()
             }
            }
            }

    }
}