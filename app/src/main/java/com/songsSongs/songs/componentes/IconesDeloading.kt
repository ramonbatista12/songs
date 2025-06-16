package com.songsSongs.songs.componentes

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.songsSongs.songs.R
import com.songsSongs.songs.ui.theme.DarkPink
import kotlinx.coroutines.delay

@Composable
fun LoadingListaMusicas(modifier: Modifier = Modifier){
    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink, MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end = Offset(cor.value,cor.value)
    )

    Row(modifier = modifier,verticalAlignment = Alignment.CenterVertically) {
        /*  Box(
              modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp).drawBehind {
                  drawCircle(brush = brushAnimado)

              })*/
        Icon(painter = painterResource(id = R.drawable.inomeado),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp),
             tint = DarkPink
        )
        Spacer(Modifier.padding(8.dp))
        Row {
            Column{
                Box(modifier.width(190.dp).height(10.dp).drawBehind {
                    drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
                })
                Spacer(Modifier.padding(8.dp))w
                Box(modifier.width(100.dp).height(10.dp).drawBehind {
                    drawRoundRect( brush = brushAnimado, cornerRadius = CornerRadius(10f,10f))
                })
            }

        }
    }



}



@Composable
fun LoadingListaMusicasColunas(modifier: Modifier=Modifier){
    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end =Offset(cor.value,cor.value)
    )
    Column(modifier = modifier.wrapContentWidth()) {
        Icon(painter = painterResource(id = R.drawable.inomeado),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp), tint = DarkPink)

        Spacer(Modifier.padding(8.dp))
        Row {
            Column{
                Box(modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect( brush = brushAnimado, cornerRadius = CornerRadius(10f,10f))
                })
                Spacer(Modifier.padding(8.dp))
                Box(modifier.width(100.dp).height(20.dp).drawBehind {
                    drawRoundRect( brush = brushAnimado, cornerRadius = CornerRadius(10f,10f))
                })

            }

        }
    }
}

@Composable
fun LoadingListaAlbums(modifier: Modifier=Modifier) {


        val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
        val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
        val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
        val cor=infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = tamanho.dp.value,
            animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
        )
        val brushAnimado= Brush.linearGradient(
            colors = brush,
            start = Offset.Zero,
            end =Offset(cor.value,cor.value)
        )

        Row(modifier = modifier.padding(10.dp),horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Image(painter = painterResource(id = R.drawable.baseline_album_24), contentDescription = null,modifier = Modifier.clip(
                RoundedCornerShape(15.dp)
            ).size(80.dp))
            Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
                Spacer(Modifier.padding(8.dp))
                Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect( brush = brushAnimado, cornerRadius = CornerRadius(10f,10f))
                })
                Spacer(Modifier.padding(3.dp))
                Box(Modifier.width(90.dp).height(20.dp).drawBehind {
                    drawRoundRect( brush = brushAnimado, cornerRadius = CornerRadius(10f,10f))
                })

            }
        }

}


@Composable
fun LoadingAlbumsColuna(modifier: Modifier=Modifier){

    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end =Offset(cor.value,cor.value)
    )

    Column(modifier =modifier.padding(10.dp).wrapContentSize()) {
        Image(painter = painterResource(id = R.drawable.baseline_album_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Spacer(Modifier.padding(8.dp))
        Row {
            Column{
                Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
                })
                Spacer(Modifier.padding(8.dp))
                Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
                })

            }

        }
    }
}

@Composable
fun LoadingListaPlaylists(modifier: Modifier=Modifier){

    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end =Offset(cor.value,cor.value)
    )

    Column(modifier =modifier.padding(10.dp).wrapContentSize()) {
        Image(painter = painterResource(id = R.drawable.baseline_playlist_play_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Spacer(Modifier.padding(8.dp))
        Row {
            Column{
                Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
                })


            }

        }
    }

}


@Composable
fun LoadingItemsArtistas(modifier: Modifier=Modifier){


    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end =Offset(cor.value,cor.value)
    )
    Row(modifier = modifier.padding(10.dp),horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Image(painter = painterResource(id = R.drawable.baseline_artistas_24), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
            Spacer(Modifier.padding(8.dp))
            Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
            })
            Spacer(Modifier.padding(3.dp))
            // Text("Nome do Artista",maxLines = 1,fontSize = 14.sp, color = DarkPink)

        }
    }
}

@Composable
fun LoadingItemsArtistasColuna(modifier: Modifier=Modifier){

    val tamanho= with(LocalDensity.current) { 100.dp.toPx() }
    val infiniteTransition= rememberInfiniteTransition(label = "caregamento_da_lista")
    val brush=listOf(MaterialTheme.colorScheme.background, DarkPink,MaterialTheme.colorScheme.background)
    val cor=infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = tamanho.dp.value,
        animationSpec = InfiniteRepeatableSpec(tween(2000, easing = LinearEasing),repeatMode = RepeatMode.Restart)
    )
    val brushAnimado= Brush.linearGradient(
        colors = brush,
        start = Offset.Zero,
        end =Offset(cor.value,cor.value)
    )
    Column(modifier =modifier.padding(10.dp).wrapContentSize()) {
        Image(painter = painterResource(id = R.drawable.baseline_artistas_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Row {
            Column{
                Box(Modifier.width(190.dp).height(20.dp).drawBehind {
                    drawRoundRect(brush = brushAnimado,cornerRadius = CornerRadius(10f,10f))
                })
                // Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun previaLoadingListaMusicas(){
    val stado= remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true){
        delay(10000)
        stado.value=!stado.value}

    }
    Scaffold(Modifier.fillMaxSize().safeGesturesPadding().safeDrawingPadding().safeContentPadding()) {
        Box(Modifier.padding(it).fillMaxSize()){}
        LazyVerticalGrid(columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(if (stado.value)3 else 1),modifier = Modifier.fillMaxSize()) {
          if(stado.value)  items(5){
              LoadingListaMusicasColunas()
            }
          else items(5){
              LoadingListaMusicas()
          }

        }
    }



}

@Composable
@Preview(showBackground = true)
fun previaLoadingListaAlbums(){
    val stado= remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true){
            delay(10000)
            stado.value=!stado.value}

    }
    Scaffold(Modifier.fillMaxSize().safeGesturesPadding().safeDrawingPadding().safeContentPadding()) {
        Box(Modifier.padding(it).fillMaxSize()){}
        LazyVerticalGrid(columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(if (stado.value)3 else 1),modifier = Modifier.fillMaxSize()) {
            if(stado.value)  items(5){
                LoadingAlbumsColuna()
            }
            else items(5){
                LoadingListaAlbums()
            }

        }
    }



}

@Composable
@Preview(showBackground = true)
fun previaLoadingPlylists(){
    val stado= remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true){
            delay(10000)
            stado.value=!stado.value}

    }
    Scaffold(Modifier.fillMaxSize().safeGesturesPadding().safeDrawingPadding().safeContentPadding()) {
        Box(Modifier.padding(it).fillMaxSize()){}
        LazyVerticalGrid(columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(if (stado.value)3 else 1),modifier = Modifier.fillMaxSize()) {
            if(stado.value)  items(5){
                LoadingListaPlaylists()
            }
            else items(5){
                LoadingListaPlaylists()
            }

        }
    }



}

@Composable
@Preview(showBackground = true)
fun previaLoadingArtistas(){
    val stado= remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true){
            delay(10000)
            stado.value=!stado.value}

    }
    Scaffold(Modifier.fillMaxSize().safeGesturesPadding().safeDrawingPadding().safeContentPadding()) {
        Box(Modifier.padding(it).fillMaxSize()){}
        LazyVerticalGrid(columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(if (stado.value)3 else 1),modifier = Modifier.fillMaxSize()) {
            if(stado.value)  items(5){
                LoadingItemsArtistasColuna()
            }
            else items(5){
                LoadingItemsArtistas()
            }

        }
    }



}