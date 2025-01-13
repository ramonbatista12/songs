package com.example.songs.componentes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.paineis.ComponetesCompartilhados

/*
* aqui esta  a representacao do Plyer redusido
* que permite o usuario ver qual musica esta em reproducao no momento enquanto ele navega em outras telas
* dentro do app
* */

@Composable
fun Miniplayer(modifier: Modifier = Modifier,text:String="Miniplayer",windoSizeClass: WindowSizeClass){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"
    val largura=if(windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.6f
          else  if (windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) 0.6f
                else 0.6f



    val infiniteTransition =rememberInfiniteTransition()

    val listaAnimacao=text.mapIndexed {indesi,caractere->
        if(caractere ==' ') remember { mutableStateOf(0f) }
        else infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = InfiniteRepeatableSpec(animation = tween(durationMillis = 10000,
                                                                     delayMillis = (1000*indesi),
                                                                     easing= EaseInCirc),
                                                                     repeatMode = RepeatMode.Restart)
        )
    }

    Row(modifier = modifier,verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier=Modifier.size(50.dp))
        Column {

            Text(text = text,maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace, modifier = Modifier.fillMaxWidth(largura))
            Text(text = "Nome do Artista", fontSize = 10.sp,maxLines = 1, overflow = TextOverflow.Ellipsis)

        }
        IconButton(onClick = {}) {
            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null)
        }
        IconButton(onClick = {}) {
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null)
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniplayerParaTransicao(modifier: Modifier = Modifier,text:String="Miniplayer",sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope: AnimatedVisibilityScope){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"

  with(sharedTransitionScope){

    Row(modifier = modifier,verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier=Modifier.size(50.dp).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))
        Column {

            Text(text = text,maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace, modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope))
            Text(text = "Nome do Artista", fontSize = 10.sp,maxLines = 1, overflow = TextOverflow.Ellipsis,modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))

        }
        IconButton(onClick = {},modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope)) {
            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null)
        }
        IconButton(onClick = {}, modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope)){
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                  contentDescription = null)
        }
    }}
}

@Preview(showBackground = true)
@Composable
fun PreviewMiniplayer(){
    Box(modifier = Modifier.fillMaxSize()) {
        Miniplayer(Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp), windoSizeClass =
            currentWindowAdaptiveInfo().windowSizeClass
        )
    }

}