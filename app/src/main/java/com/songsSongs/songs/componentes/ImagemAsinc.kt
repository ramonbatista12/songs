package com.songsSongs.songs.componentes

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.songsSongs.songs.R
import com.songsSongs.songs.ui.theme.DarkPink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.jetbrains.annotations.Async

@Composable
fun AsyncImage(modifier: Modifier = Modifier,acaoCarregamento:suspend ()->Bitmap?){
    val coroutineScope= rememberCoroutineScope()
    val resultado= remember { mutableStateOf<ResultadoLoadImage?>(ResultadoLoadImage.Carregando) }
  DisposableEffect (Unit) {
      coroutineScope.launch(Dispatchers.IO) {
          delay(100)
          val bitmap= coroutineScope.async { acaoCarregamento() }.await()
          if(bitmap==null) resultado.value=ResultadoLoadImage.Erro
          else resultado.value=ResultadoLoadImage.Ok(bitmap)
      }
      onDispose {
          resultado.value =null
      }
  }

    when(val result=resultado.value){
        is ResultadoLoadImage.Carregando->{
            LoadImage(modifier = modifier)
        }

        is ResultadoLoadImage.Ok->{
            val bitmap=result.bitmap.asImageBitmap()
            androidx.compose.foundation.Image(bitmap = bitmap,
                                             contentDescription = null,
                                             modifier=modifier.clip(RoundedCornerShape(10.dp)),
                                             contentScale = ContentScale.FillBounds)

        }
        else ->{
            Icon(painterResource(id = R.drawable.inomeado),
                tint = DarkPink,
                contentDescription = null,
                modifier=modifier.clip(RoundedCornerShape(10.dp))
                                  .border(width = 1.dp,
                                          color = MaterialTheme.colorScheme.onBackground,
                                          shape = RoundedCornerShape(10.dp)))
        }
    }

}

@Composable
fun AsyncImage(modifier: Modifier = Modifier,idDefautIcon:Int,acaoCarregamento:suspend ()->Bitmap?){
    val coroutineScope= rememberCoroutineScope()
    val resultado= remember { mutableStateOf<ResultadoLoadImage?>(ResultadoLoadImage.Carregando) }
    DisposableEffect (Unit) {
        coroutineScope.launch(Dispatchers.IO) {
           // delay(100)
            val bitmap= coroutineScope.async { acaoCarregamento() }.await()
            if(bitmap==null) resultado.value=ResultadoLoadImage.Erro
            else resultado.value=ResultadoLoadImage.Ok(bitmap!!)
        }
        onDispose {
            resultado.value =null
        }
    }

    when(val result=resultado.value){
        is ResultadoLoadImage.Carregando->{
            LoadImage(modifier = modifier)
        }

        is ResultadoLoadImage.Ok->{
            val bitmap=result.bitmap.asImageBitmap()
            androidx.compose.foundation.Image(bitmap = bitmap,
                contentDescription = null,
                modifier=modifier.clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds)

        }
        else ->{
            Icon(painterResource(id = idDefautIcon),
                tint = DarkPink,
                contentDescription = null,
                modifier=modifier.clip(RoundedCornerShape(10.dp))
                    .border(width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(10.dp)))
        }
    }

}

 @Composable
 fun LoadImage(modifier: Modifier = Modifier){
     val tamanhoCoreto =with(LocalDensity.current){
         100.dp.toPx()
     }
     val  infiniteTransition = rememberInfiniteTransition(label = "ImagemLoad")
     val cores= listOf<Color>(Color.DarkGray,MaterialTheme.colorScheme.background,Color.DarkGray,MaterialTheme.colorScheme.background)
     val animacao=infiniteTransition.animateFloat(initialValue = 0f,
                                                  targetValue = tamanhoCoreto,
                                                  animationSpec = InfiniteRepeatableSpec(animation = tween(delayMillis = 500, easing = LinearEasing),
                                                                                         repeatMode = RepeatMode.Restart),
                                                  label = "animacao")
     val coresAnimads =Brush.linearGradient(colors = cores, start = Offset(x = 30f, y = 20f), end = Offset(x = animacao.value, y = animacao.value))

     Box(modifier = modifier.size(80.dp)
                            .background(coresAnimads)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(width = 1.dp,color =MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(10.dp)))

 }



sealed class ResultadoLoadImage(){
    data class Ok(val bitmap: Bitmap):ResultadoLoadImage()
    object Erro:ResultadoLoadImage()
    object Carregando:ResultadoLoadImage()
}
