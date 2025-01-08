package com.example.songs.componentes

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
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
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/*
* aqui esta  a representacao do Plyer redusido
* que permite o usuario ver qual musica esta em reproducao no momento enquanto ele navega em outras telas
* dentro do app
* */

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Miniplayer(vm:VmodelPlayer,modifier: Modifier = Modifier,text:String="Miniplayer",windoSizeClass: WindowSizeClass){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"
    val largura=if(windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.6f
          else  if (windoSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) 0.6f
                else 0.6f

   val scop = rememberCoroutineScope()
   val plyerEmreproducao=vm._emreproducao.collectAsState(false)

   val metaData=vm._metadataAtual.collectAsState()
   val bitmap=remember{ mutableStateOf<Bitmap?>(null)}
   val context = LocalContext.current

    LaunchedEffect(key1=metaData.value?.mediaMetadata?.artworkUri) {
        scop.launch {

                scop.launch(Dispatchers.IO){
                    try {
                        Log.d("tentando metadata","chamada do try")
                        bitmap.value =  getMetaData(metaData!!.value!!.mediaMetadata!!.artworkUri!!,metaData!!.value!!.mediaId!!.toLong(),context,"miniplyer")
                    }catch (e:Exception){
                        Log.d("tentando metadata","essecao ")
                        bitmap.value=null

                    }
                }

        }

    }




    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        if(bitmap.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink),
              contentDescription = null,
              modifier=Modifier.size(50.dp))
        else{
            val bit=bitmap.value!!.asImageBitmap()
            Image(bitmap = bit,
                  contentDescription = null,
                  modifier=Modifier.size(50.dp))
        }
       Spacer(Modifier.padding(3.dp))
       Column {

            Text(text =if(metaData.value!=null) metaData.value!!.mediaMetadata.title.toString() else text ,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis,
                 fontFamily = FontFamily.Monospace,
                 modifier = Modifier.fillMaxWidth(largura))
            Text(text = "Nome do Artista",
                 fontSize = 10.sp,
                 maxLines = 1,
                 overflow = TextOverflow.Ellipsis)

        }
        IconButton(onClick = {
            if (plyerEmreproducao.value){
                vm.pause()
            }
            else vm.play()

        }) {
            if(plyerEmreproducao.value)
                Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                     contentDescription = null)
            else
                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                     contentDescription = null)
        }
        IconButton(onClick = {}) {
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                 contentDescription = null)
        }
    }
}
sealed class EstadoMiniplayer{
    object caregando
    object comcluido
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MiniplayerParaTransicao(modifier: Modifier = Modifier,text:String="Miniplayer",sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope: AnimatedVisibilityScope,vm: VmodelPlayer){
    val texto = "Miniplayer Nome da Musica"
    val texto2 = "Nome do Artista"
    val metadata=vm._metadataAtual.collectAsState()
    val  emplyer =vm._emreproducao.collectAsState()
    val bitmap=remember{ mutableStateOf<Bitmap?>(null)}
    val scope= rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = metadata.value) {
    scope.launch (Dispatchers.IO){
        try {
            bitmap.value= getMetaData( metadata!!.value!!.mediaMetadata!!.artworkUri!!,metadata.value!!.mediaId.toLong(),context)
        }catch (e:Exception){
            bitmap.value=null
        }
    }
    }

  with(sharedTransitionScope){

    Row(modifier = modifier,verticalAlignment = Alignment.CenterVertically) {
        if (bitmap.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier=Modifier.size(50.dp).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))
        else{
            val bitmap=bitmap.value!!.asImageBitmap()
            Image(bitmap = bitmap,
                  contentDescription = null,
                  modifier=Modifier.size(50.dp).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope))
        }
        Spacer(Modifier.padding(3.dp))
        Column {

            Text(text =if (metadata.value==null)text else metadata!!.value!!.mediaMetadata.title.toString() ,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.width(200.dp)
                                   .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope))
            Text(text = if (metadata.value==null)text else "Desconhecido",
                fontSize = 10.sp,maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))

        }
        IconButton(onClick = {
            if (emplyer.value) scope.launch { vm.pause() }
            else scope.launch { vm.play() }},
                  modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope)) {
            if (emplyer.value)
            Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                 contentDescription = null)
            else
                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                     contentDescription = null)
        }
        IconButton(onClick = {},
                   modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope)){
            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                  contentDescription = null)
        }
    }}
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun PreviewMiniplayer(){
    Box(modifier = Modifier.fillMaxSize()) {
        val conecao = MutableStateFlow<ResultadosConecaoServiceMedia>(ResultadosConecaoServiceMedia.Desconectado)
        val vm = VmodelPlayer(estadoService = conecao)
        Miniplayer(vm,Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp), windoSizeClass =
            currentWindowAdaptiveInfo().windowSizeClass
        )
    }

}