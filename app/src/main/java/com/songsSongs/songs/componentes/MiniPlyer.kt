package com.songsSongs.songs.componentes

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.paineis.ComponetesCompartilhados
import com.songsSongs.songs.ui.theme.DarkPink
import com.songsSongs.songs.viewModels.ImagemPlyer
import com.songsSongs.songs.viewModels.ViewModelListas
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/*
* aqui esta  a representacao do Plyer redusido
* que permite o usuario ver qual musica esta em reproducao no momento enquanto ele navega em outras telas
* dentro do app
* */

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Miniplayer(modifier: Modifier = Modifier,text:String="Miniplayer",
               windoSizeClass: WindowSizeClass,
               vmListas: ViewModelListas,
               vm: VmodelPlayer){

    val largura=if(windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.6f
          else  if (windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) 0.6f
                else 0.6f

    val metadata =vm._mediaItemAtual.collectAsState()

    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    val reprodusind=vm._emreproducao.collectAsState()
    val backgraudColorSquemas =MaterialTheme.colorScheme.background
    val textColorSquemas=MaterialTheme.colorScheme.onBackground
    val corDoBackgrand = remember { mutableStateOf<Color>(backgraudColorSquemas) }
    val corTexto=remember { mutableStateOf<Color>(Color.Black) }
    val imagem=vm._imagemPlyer.collectAsState()
    //val cores=remember { mutableStateOf<List<Color>?>(null) }
   // val int=MaterialTheme.colorScheme.background.value.toInt()
    LaunchedEffect(metadata.value) {
        scope.launch {
            val bitmap =scope.async(Dispatchers.IO) { vmListas.getImageBitMap(metadata.value!!.mediaMetadata!!.artworkUri!!) }.await()
            AuxiliarMudancaDeBackGrands().mudarBackgrandMiniPlyer(bitmap,
                backgraudColor=corDoBackgrand,
                corTexto  = corTexto,
                textColorSquemas = textColorSquemas,
                backgraudColorSquemas = backgraudColorSquemas)
            vm.caregarImagePlyer(bitmap)
        }




    }




    DisposableEffect(Unit){
        onDispose {

            scope.cancel()
        }
    }





    Row(modifier = modifier.clip(RoundedCornerShape(15.dp))
                           .border(width = 1.5.dp,
                                   brush =SolidColor(textColorSquemas) ,
                                   shape = RoundedCornerShape(15.dp))
                           .background(corDoBackgrand.value),
        verticalAlignment = Alignment.CenterVertically) {
        when(val r =imagem.value){
            is ImagemPlyer.Vazia-> Icon(painter = painterResource(id = r.icone),
                                         contentDescription = null,
                                          modifier=Modifier.size(50.dp)
                                                           .clip(RoundedCornerShape(15.dp))
                                                           .border(width = 1.5.dp,
                                                                   brush = SolidColor(corTexto.value),
                                                                   shape = RoundedCornerShape(15.dp)),tint = DarkPink)
            is ImagemPlyer.Imagem->{
                val _bitMap=r.imagem.asImageBitmap()
                Image(bitmap = _bitMap,
                    contentDescription = null,
                    modifier=Modifier.size(50.dp).clip(
                        RoundedCornerShape(10.dp)
                    ), contentScale = ContentScale.FillBounds)}
        }



        Spacer(Modifier.padding(3.dp))
        Column {
   Text(text = if(metadata.value==null) text else metadata.value!!.mediaMetadata.title.toString(),
                 maxLines = 1,
                 color = corTexto.value,
                 fontFamily = FontFamily.Monospace,
                 modifier = Modifier.fillMaxWidth(largura).basicMarquee(iterations = 10, initialDelayMillis = 1000, repeatDelayMillis = 1000))
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


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniplayerParaTransicao(modifier: Modifier = Modifier,
                            text:String="Miniplayer",
                            sharedTransitionScope: SharedTransitionScope,
                            animatedVisibilityScope: AnimatedVisibilityScope,
                            vm: VmodelPlayer,vmListas: ViewModelListas,
                            corDotexto:Color,
                            acaoMudarBackgraud:suspend (bitmap:android.graphics.Bitmap?)->Unit,
                            backgraud:Color=MaterialTheme.colorScheme.background,
                            ){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"
    val scope : CoroutineScope= rememberCoroutineScope()
    val  metadata= vm._mediaItemAtual.collectAsState()
    val reproduzindo=vm._emreproducao.collectAsState()
    val imagem=vm._imagemPlyer.collectAsState()
    val context= LocalContext.current

    val texttColorSchemas =MaterialTheme.colorScheme.onBackground
    LaunchedEffect(metadata.value) {
       scope.launch {
           val bitmap =scope.async(Dispatchers.IO) { vmListas.getImageBitMap(metadata.value!!.mediaMetadata!!.artworkUri!!)  }.await()
           vm.caregarImagePlyer(bitmap)
           acaoMudarBackgraud(bitmap)

       }



    }
    DisposableEffect(Unit) {
        onDispose {

            scope.cancel()
        }
    }

  with(sharedTransitionScope){

    Row(modifier = modifier.clip(RoundedCornerShape(15.dp))
                           .border(width =1.5.dp,
                                   brush =SolidColor(texttColorSchemas) ,
                                   shape = RoundedCornerShape(15.dp))
                           .background(backgraud),
        verticalAlignment = Alignment.CenterVertically) {
        when(val r =imagem.value){
            is ImagemPlyer.Vazia->Icon(painter = painterResource(id = r.icone),
                contentDescription = null, tint = DarkPink,
                modifier=Modifier.size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(width = 1.5.dp,brush = SolidColor(corDotexto),shape = RoundedCornerShape(15.dp))
                    .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))
            is ImagemPlyer.Imagem->{
                val _bitMap=r.imagem.asImageBitmap()
                Image(bitmap= _bitMap,
                    contentDescription = null,
                    modifier=Modifier.size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope),
                    contentScale = ContentScale.FillBounds)
            }
        }

        Spacer(Modifier.padding(2.dp))
        Column {

            Text(text = if (metadata.value==null)text else metadata.value!!.mediaMetadata.title.toString(),
                maxLines = 1,color = corDotexto,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.fillMaxWidth(0.5f)
                                   .basicMarquee(iterations = 10, initialDelayMillis = 1000, repeatDelayMillis = 1000)
                                   .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope)
                                   )
            Text(text = if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                fontSize = 10.sp, color = corDotexto,
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
            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null, tint = corDotexto)
            else
            Icon(painter = painterResource(id = R.drawable.baseline_pause_24), contentDescription = null,tint = corDotexto)
        }
        IconButton(onClick = {
            scope.launch {
                vm.next()
            }
        }, modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope)){
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), tint = corDotexto,
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