package com.example.songs.componentes

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.songs.R


@Composable
fun Miniplayer(modifier: Modifier = Modifier,text:String="Miniplayer"){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"




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

            Text(text = text,maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace)
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

@Preview(showBackground = true)
@Composable
fun PreviewMiniplayer(){
    Box(modifier = Modifier.fillMaxSize()) {
        Miniplayer(Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp))
    }

}