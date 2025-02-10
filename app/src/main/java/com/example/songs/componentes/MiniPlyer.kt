package com.example.songs.componentes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.alpha
import androidx.core.graphics.red
import androidx.palette.graphics.Palette
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.paineis.ComponetesCompartilhados
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/*
* aqui esta  a representacao do Plyer redusido
* que permite o usuario ver qual musica esta em reproducao no momento enquanto ele navega em outras telas
* dentro do app
* */

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Miniplayer(modifier: Modifier = Modifier,text:String="Miniplayer",windoSizeClass: WindowSizeClass,vm: VmodelPlayer){

    val largura=if(windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.6f
          else  if (windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) 0.6f
                else 0.6f

    val metadata =vm._mediaItemAtual.collectAsState()
    val bitmap= remember { mutableStateOf<android.graphics.Bitmap?>(null)  }
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    val reprodusind=vm._emreproducao.collectAsState()
    val backgraudColor =MaterialTheme.colorScheme.background.value.toInt()
    val textColorSquemas=MaterialTheme.colorScheme.scrim
    val cor = remember { mutableStateOf(Color(backgraudColor)) }
    val corTexto=remember { mutableStateOf(Color.Black) }
    //val cores=remember { mutableStateOf<List<Color>?>(null) }
   // val int=MaterialTheme.colorScheme.background.value.toInt()
    LaunchedEffect(metadata.value) {
        scope.launch(Dispatchers.IO) {
            try {
                bitmap.value= getMetaData(context = context,uri = metadata.value!!.mediaMetadata.artworkUri!!,id = metadata.value!!.mediaId.toLong())
                if(bitmap.value!=null){
                val palette= Palette.from(bitmap.value!!).generate()
                val int =palette.getMutedColor(backgraudColor)
                cor.value=Color(int)
                corTexto.value= Color(palette.mutedSwatch?.bodyTextColor?: textColorSquemas.value.toInt())
                }
                else{
                    cor.value=Color(backgraudColor)
                    corTexto.value=textColorSquemas
                }
            }catch (e:Exception){
                Log.e("Load tumbmail",e.message.toString())
                bitmap.value=null
            }

            }

    }




    DisposableEffect(Unit){
        onDispose {
            bitmap.value=null
            scope.cancel()
        }
    }






    Row(modifier = modifier.clip(RoundedCornerShape(15.dp)).background(cor.value),verticalAlignment = Alignment.CenterVertically) {
        if (bitmap.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink),
             contentDescription = null,
             modifier=Modifier.size(50.dp))
       else{
           val _bitMap=bitmap.value!!.asImageBitmap()
           Image(bitmap = _bitMap,
                contentDescription = null,
               modifier=Modifier.size(50.dp).clip(
               RoundedCornerShape(10.dp)
           ))
       }
        Spacer(Modifier.padding(3.dp))
        Column {
   Text(text = if(metadata.value==null) text else metadata.value!!.mediaMetadata.title.toString(),
                 maxLines = 1,
                 color = corTexto.value,
                 fontFamily = FontFamily.Monospace,
                 modifier = Modifier.fillMaxWidth(largura))
        Text(text = if (metadata.value==null)"Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                 fontSize = 10.sp,
                 color = corTexto.value,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis,modifier=Modifier.fillMaxWidth(largura))

        }
        IconButton(onClick = {
            scope.launch {
                if (reprodusind.value)vm.pause()
                else vm.play()
            }
        }) {
            if(reprodusind.value)
            Icon(painter = painterResource(id = R.drawable.baseline_pause_24), contentDescription = null, tint = corTexto.value)
            else
            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null, tint = corTexto.value)
        }
        IconButton(onClick = {
            scope.launch {
                vm.next()
            }
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null, tint = corTexto.value)
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniplayerParaTransicao(modifier: Modifier = Modifier,text:String="Miniplayer",sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope: AnimatedVisibilityScope,vm: VmodelPlayer){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"
    val bitmap= remember { mutableStateOf<android.graphics.Bitmap?>(null)  }
    val  metadata= vm._mediaItemAtual.collectAsState()
    val reproduzindo=vm._emreproducao.collectAsState()

    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    LaunchedEffect(metadata.value) {
        scope.launch(Dispatchers.IO) {
            try {
                bitmap.value= getMetaData(context = context,uri = metadata.value!!.mediaMetadata.artworkUri!!,id = metadata.value!!.mediaId.toLong())
            }catch (e:Exception){
                bitmap.value=null
            }

        }
    }
    DisposableEffect(Unit) {
        onDispose {
            bitmap.value=null
            scope.cancel()
        }
    }

  with(sharedTransitionScope){

    Row(modifier = modifier,verticalAlignment = Alignment.CenterVertically) {
        if (bitmap.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink),
              contentDescription = null,
              modifier=Modifier.size(50.dp).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))
        else{
            val _bitMap=bitmap.value!!.asImageBitmap()
            Image(bitmap= _bitMap,
                 contentDescription = null,
                 modifier=Modifier.size(50.dp)
                                  .clip(RoundedCornerShape(10.dp))
                                  .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))

        }
        Spacer(Modifier.padding(2.dp))
        Column {

            Text(text = if (metadata.value==null)text else metadata.value!!.mediaMetadata.title.toString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope)
                                   .fillMaxWidth(0.5f))
            Text(text = if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope)
                                   .fillMaxWidth(0.3f))

        }
        IconButton(onClick = {
            if (reproduzindo.value)
                scope.launch {
                    vm.pause()
                }
            else
                scope.launch {
                    vm.play()
                }
        },
                  modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope)) {
            if(!reproduzindo.value)
            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null)
            else
            Icon(painter = painterResource(id = R.drawable.baseline_pause_24), contentDescription = null)
        }
        IconButton(onClick = {
            scope.launch {
                vm.next()
            }
        }, modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope)){
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                  contentDescription = null)
        }
    }}
}

@Preview(showBackground = true)
@Composable
fun PreviewMiniplayer(){
    //Box(modifier = Modifier.fillMaxSize()) {
     //   Miniplayer(Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp), windoSizeClass =
     //       currentWindowAdaptiveInfo().windowSizeClass
      //  )
   // }

}