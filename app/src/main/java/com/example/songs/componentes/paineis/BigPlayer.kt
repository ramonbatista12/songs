package com.example.songs.componentes.paineis

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.MiniplayerParaTransicao
import com.example.songs.componentes.getMetaData
import com.example.songs.repositorio.RepositorioService
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme
import com.example.songs.viewModels.ModoDerepeticao
import com.example.songs.viewModels.ViewModelListas
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
* BigPlyer representa o player em si aonde se pode ver os dados da musica em reproducao no momento
* e responsavel por medir e determinar como deve ser esibido em cado tamanho de tela
* */

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BigPlayer(modifier: Modifier = Modifier,
              windowSizeClass: WindowSizeClass,
              paddingValues: PaddingValues,
              vm: VmodelPlayer,vmlista: ViewModelListas,
              acaoAvisoBigplyer:()->Unit,acaoDeVoutar: () -> Unit){
   LaunchedEffect(Unit) {
       acaoAvisoBigplyer()
   }
    DisposableEffect(Unit) {

        onDispose {
            acaoAvisoBigplyer()
        }
    }

SelecaoDosPlyer(modifier,windowSizeClass,paddingValues,vm,vmlista,acaoAvisoBigplyer,acaoDeVoutar=acaoDeVoutar)




}


@Composable
fun SelecaoDosPlyer(modifier: Modifier = Modifier,
                    windowSizeClass: WindowSizeClass,
                    paddingValues: PaddingValues,
                    vm: VmodelPlayer,vmlista: ViewModelListas,
                    acaoAvisoBigplyer:()->Unit,acaoDeVoutar: () -> Unit={}){
    val int =MaterialTheme.colorScheme.background.value.toInt()
    val coresBackgrad=remember { mutableStateOf<List<Color>?>(null) }
    if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
        PlayerCompat(modifier=modifier,
            vm = vm, vmlista = vmlista,acaoDeVoutar=acaoDeVoutar)

    else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM)
        if(windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.MEDIUM ||windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.EXPANDED)PlayerCompat(modifier, vm = vm,vmlista = vmlista,acaoDeVoutar=acaoDeVoutar)
        else  PlyerEspandido(modifier,windowSizeClass,vm=vm,vmlista)
    else  PlyerEspandido(modifier,windowSizeClass,vm=vm,vmlista)
}



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Plyer(modifier: Modifier=Modifier,
          sharedTransitionScope: SharedTransitionScope,
          animatedVisibilityScope: AnimatedVisibilityScope,
          vm:VmodelPlayer,
          acaoMudarBackgraud:suspend (bitmap:Bitmap?)->Unit={},
          cor:Color=MaterialTheme.colorScheme.onBackground){

    val bitMap =remember{ mutableStateOf<Bitmap?>(null)}
    val mediaItem=vm._mediaItemAtual.collectAsState()
    val tempoTotal=vm._tempoTotal.collectAsState()
    val reproduzindo=vm._emreproducao .collectAsState()
    val modoAleatorio=vm._modoAleatorio.collectAsState()
    val modoRepeticao=vm._modoRepeticao.collectAsState()
    val duracao=vm._duracao.collectAsState()
    val duracaoString=vm._tempoTotalString.collectAsState()
    val tempoTotalString=vm._duracaoString.collectAsState()
    val context= LocalContext.current
    val scop=rememberCoroutineScope()
    LaunchedEffect(mediaItem.value){
        scop.launch(Dispatchers.IO) {
            try {
               bitMap.value= getMetaData(context = context,uri = mediaItem.value!!.mediaMetadata.artworkUri!!,id = mediaItem!!.value!!.mediaId.toLong())
               acaoMudarBackgraud(bitMap.value)
            }catch (e:Exception){
                bitMap.value=null
            }

        }

    }
    DisposableEffect(Unit){
        onDispose {
            bitMap.value=null
            scop.cancel()
        }
    }

    with(sharedTransitionScope){
        Column(modifier = modifier
            .padding(10.dp)
            .background(color =Color.Transparent),
               horizontalAlignment = Alignment.CenterHorizontally) {
            if(bitMap.value==null)
            Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),
                 contentDescription = null,
                 tint = DarkPink,
                 modifier = Modifier
                     .size(250.dp)
                     .clip(RoundedCornerShape(15.dp))
                     .sharedElement(
                         rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),
                         animatedVisibilityScope
                     ),)
            else{
                val _bitmap=bitMap.value!!.asImageBitmap()
                Image(bitmap=_bitmap,contentDescription = null,
                    modifier = Modifier
                        .size(250.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .sharedElement(
                            rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),
                            animatedVisibilityScope
                        ),)
            }

            Spacer(Modifier.padding(10.dp))
            Column(modifier = Modifier) {

                Text(text =if(mediaItem.value==null) "Nome da Musica" else mediaItem.value!!.mediaMetadata.title.toString(),
                     modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope),
                    color = cor,
                      maxLines = 2,
                     fontFamily = FontFamily.Monospace)
                Spacer(Modifier.padding( 3.dp))
                Text(text = if (mediaItem.value==null) "Nome do Artista" else mediaItem.value!!.mediaMetadata.artist.toString(), color = cor,
                     modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))
                Spacer(Modifier.padding(10.dp))
                Column(modifier = Modifier.width(400.dp)) {

                   Row(modifier=Modifier.fillMaxWidth()) {

                         Text(text = tempoTotalString.value,
                             color = cor, fontSize = 8.sp )
                         Text("/", fontSize = 8.sp, color = cor )
                        Text(text =duracaoString.value, fontSize = 8.sp, color = cor )

                   }

                    Slider(value = duracao.value, onValueChange = {
                        scop.launch {
                            val valor=(it*tempoTotal.value)/100f
                            vm.seekTo(valor.toLong())
                        }
                    },
                         colors = SliderDefaults.colors(activeTrackColor = cor),
                         valueRange = 0f..100f,
                         modifier = Modifier.height(10.dp))
                    Spacer(Modifier.padding(10.dp))

                    Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                    IconButton({scop.launch { vm.preview() }}) {
                        Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = cor)
                    }
                    IconButton({
                        scop.launch {
                           vm.setModoAleatorio(!modoAleatorio.value)}
                    }) {
                        if(!modoAleatorio.value)
                        Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = cor)
                        else
                        Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24), contentDescription = null,tint = cor)
                        }

                    IconButton({
                        scop.launch {
                            if(reproduzindo.value)
                                vm.pause()
                            else vm.play()
                        }

                    }) {
                        if (reproduzindo.value)
                        Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                            contentDescription = null,
                            tint = cor,
                            modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope))
                        else
                            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                contentDescription = null,
                                tint = cor,
                                modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope))
                        }

                        IconButton({
                            scop.launch {
                                when(modoRepeticao.value){
                                    is ModoDerepeticao.Desativado->vm.setModoDeRepeticao(ModoDerepeticao.RepetirEssa)
                                    is ModoDerepeticao.RepetirEssa->vm.setModoDeRepeticao(ModoDerepeticao.RepetirTodos)
                                    is ModoDerepeticao.RepetirTodos->vm.setModoDeRepeticao(ModoDerepeticao.Desativado)
                                }
                            }


                        }) {
                            when(modoRepeticao.value){
                                is ModoDerepeticao.Desativado->Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint =cor)
                                is ModoDerepeticao.RepetirEssa->Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24), contentDescription = null,tint = cor)
                                is ModoDerepeticao.RepetirTodos->Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24), contentDescription = null,tint = cor)
                            }

                        }
                        IconButton({vm.next()}) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                contentDescription = null,
                                tint = cor,modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope))

                        }

                    }
                }
            }


        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun getMetaData(c: Context, uri: Uri, id: Long):Bitmap?{
    try {
        val resolver = c.contentResolver
        val tumbmail=resolver.loadThumbnail(uri, android.util.Size(100, 100),null)
        return tumbmail
    }catch (e:Exception){
        return null
    }

}



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ComtroladorPlyer(modifier: Modifier=Modifier,
                     sharedTransitionScope: SharedTransitionScope,
                     animatedVisibilityScope:AnimatedVisibilityScope,
                     onclick:()->Unit={},
                     vm:VmodelPlayer,
                     cor:MutableState<Color>,
                     acaoMudarBackgraud:suspend (bitmap:Bitmap?)->Unit={}){
    val listaAvberta=remember{ mutableStateOf(false)}
      with(sharedTransitionScope){
          Box(modifier = modifier.background(Color.Transparent).padding(top = 30.dp)
              .fillMaxSize()
              .imePadding()
              .sharedBounds(
                  rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),
                  animatedVisibilityScope,
                  resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
              )){
              Plyer(Modifier.align( Alignment.TopCenter).background(Color.Transparent),
                    animatedVisibilityScope = animatedVisibilityScope,sharedTransitionScope = sharedTransitionScope,
                    vm=vm, acaoMudarBackgraud = acaoMudarBackgraud,cor = cor.value)


              IconButton({
                  listaAvberta.value=!listaAvberta.value
                  onclick()
              },modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .size(70.dp)
                  .padding(5.dp)) {
                  Icon(painter = painterResource(id = R.drawable.baseline_list_24),
                       contentDescription = null, tint = cor.value, modifier = Modifier
                      .align(
                          Alignment.BottomCenter
                      )
                      .size(50.dp)
                      .padding(5.dp))

              }


          }
      }


}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerCompat(modifier: Modifier=Modifier,
                 vm:VmodelPlayer,
                 vmlista:ViewModelListas,
                 acaoMudarLista:(p:Palette)->Unit={},
                 acaoDeVoutar:()->Unit={}) {
    val listaAvberta = remember { mutableStateOf(false) }
    val backgraudColor =MaterialTheme.colorScheme.background
    val textColorSquemas=MaterialTheme.colorScheme.onBackground
    val cor = remember { mutableStateOf(Color(backgraudColor.value.toInt())) }
    val corTexto=remember { mutableStateOf(Color.Black) }

    Box(modifier = Modifier.fillMaxSize().background(cor.value)) {
      IconButton (onClick =acaoDeVoutar) {
          Icon(painter = painterResource(R.drawable.outline_west_24), contentDescription = null, tint = corTexto.value, modifier = Modifier)
      }
        SharedTransitionLayout {
            AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->

                Box(modifier = modifier
                    .fillMaxSize()
                    .imePadding()) {
                    SharedTransitionLayout {
                        AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->
                            if (!targetState) {
                                ComtroladorPlyer(modifier = Modifier.align(Alignment.TopCenter).background(Color.Transparent),sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedVisibilityScope = this@AnimatedContent
                                           ,onclick = {listaAvberta.value=!listaAvberta.value},
                                            vm=vm,cor=corTexto ,acaoMudarBackgraud = {
                                                if(it!=null){
                                                    val palette =Palette.from(it).generate()
                                                    val int=palette.getDarkMutedColor(backgraudColor.value.toInt())
                                                    cor.value=Color(int)
                                                    val luminessenciaBackgraud=cor.value.luminance()
                                                    corTexto.value=when{
                                                        (luminessenciaBackgraud == 0.0f)-> textColorSquemas
                                                        (luminessenciaBackgraud>0.0f&&luminessenciaBackgraud<0.1f) ->Color.White
                                                        (luminessenciaBackgraud>=0.1f) ->Color(palette.getVibrantColor(Color.White.value.toInt()) )

                                                        else -> Color.Unspecified
                                                    }

                                                }
                                        else {
                                            cor.value=backgraudColor
                                            corTexto.value=textColorSquemas

                                        }
                                    })

                            } else {

                                val estadoPlylist=vmlista._estadoPlylsist.collectAsState()
                                val lista =vmlista.plylist().collectAsState(emptyList()) //

                                Column(Modifier.sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),this@AnimatedContent, resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)
                                    .background(backgraudColor)) {
                                  Row (modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                      MiniplayerParaTransicao(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        vm = vm, corDotexto = corTexto.value, backgraud = cor.value,
                                          acaoMudarBackgraud = {if(it!=null){
                                              val palette =Palette.from(it).generate()
                                              val int=palette.getDarkMutedColor(backgraudColor.value.toInt())
                                              cor.value=Color(int)
                                              val luminessenciaBackgraud=cor.value.luminance()
                                              corTexto.value=when{
                                                  (luminessenciaBackgraud == 0.0f)-> textColorSquemas
                                                  (luminessenciaBackgraud>0.0f&&luminessenciaBackgraud<0.1f) ->Color.White
                                                  (luminessenciaBackgraud>=0.1f) ->Color(palette.getVibrantColor(Color.White.value.toInt()) )

                                                  else -> Color.Unspecified
                                              }

                                          }
                                          else {
                                              cor.value=backgraudColor
                                              corTexto.value=textColorSquemas

                                          }
                                          }
                                    )
                                }
                                    Spacer(Modifier.padding(0.4.dp))
                                    Row(Modifier.fillMaxWidth(),horizontalArrangement =Arrangement.Center){
                                        IconButton(onClick = {
                                        listaAvberta.value=!listaAvberta.value
                                    },modifier = Modifier.size(60.dp)) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = DarkPink,modifier=Modifier.size(50.dp))
                                    } }
                                    val  metadata=vm._mediaItemAtual.collectAsState()
                                    LazyColumn(modifier=Modifier.background(color = backgraudColor).fillMaxWidth().fillMaxHeight()) {

                                        itemsIndexed(items = lista.value) {indice,item->
                                           if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                                               Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                                   Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, tint = corTexto.value)
                                                   ItemDaLista(Modifier.clickable {
                                                       vm.seekToItem(indice)

                                                   },item = item)
                                               }
                                           else
                                            ItemDaLista(Modifier.clickable {
                                                vm.seekToItem(indice)

                                            },item = item)
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








    @SuppressLint("SuspiciousIndentation")
    @Composable
fun PlyerEspandido(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass,vm:VmodelPlayer,vmlista:ViewModelListas) {
       val bitmap= remember { mutableStateOf<Bitmap?>(null) }
       val scope= rememberCoroutineScope()
       val context= LocalContext.current
       val duracao=vm._duracao.collectAsState()
       val tempoTotal=vm._tempoTotal.collectAsState()
       val metadata=vm._mediaItemAtual.collectAsState()
       val duracaoString=vm._duracaoString.collectAsState()
       val tempoTotalString=vm._tempoTotalString.collectAsState()
       val modoAleatorio=vm._modoAleatorio.collectAsState()
       val  modoRepeticao=vm._modoRepeticao.collectAsState()
       val  reproduzindo=vm._emreproducao.collectAsState()
       val plyListAtual=vmlista.plylist().collectAsState(emptyList())
         LaunchedEffect(Unit) {
             Log.d("TAG", "PlyerEspandido: plyListAtual ${plyListAtual.value.size} ")
         }
        LaunchedEffect(metadata.value) {
            scope.launch(Dispatchers.IO) {
                try {
                    bitmap.value= getMetaData(context = context,uri = metadata.value!!.mediaMetadata.artworkUri!!,id = metadata.value!!.mediaId.toLong())
                }catch (e:Exception){
                    bitmap.value=null}
            }

        }
        DisposableEffect(Unit) {
            onDispose {
                bitmap.value=null
                scope.cancel()
            }
        }

        Row(
            modifier
                .fillMaxSize()
                .padding(bottom = 10.dp), verticalAlignment = Alignment.Top) {
            Box(
                Modifier
                    .fillMaxWidth(0.4f)

                    .padding(10.dp)) {

                Column(Modifier.align(Alignment.TopCenter)) {
                    val iconeSize =
                        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 90.dp else 400.dp
                    if(bitmap.value==null)
                    Icon(
                        painter = painterResource(R.drawable.baseline_music_note_24),
                        contentDescription = null,

                        modifier = Modifier
                            .size(iconeSize)
                            .clip(
                                RoundedCornerShape(15.dp)
                            )
                            .border(
                                width = 0.5.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .align(Alignment.CenterHorizontally), tint = DarkPink
                    )
                    else{
                        val _bitmap=bitmap.value!!.asImageBitmap()
                        Image(bitmap = _bitmap,
                              contentDescription = null ,
                              modifier = Modifier.size(iconeSize)
                            .clip(
                                RoundedCornerShape(15.dp)
                            )

                            .align(Alignment.CenterHorizontally))
                    }

                    Column {

                        Text(text = if(metadata.value==null)"Nome da Musica" else metadata.value!!.mediaMetadata.title.toString(),
                             fontFamily = FontFamily.Monospace,
                             fontSize = if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 10.sp else 18.sp,
                              maxLines = if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 1 else 2, )
                        Spacer(Modifier.padding(0.4.dp))
                        Text(text =if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                            maxLines =if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 1 else 2,
                             fontSize = if (windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.COMPACT)8.sp else 14.sp,)
                        Spacer(Modifier.padding(0.4.dp))
                        Column {

                            Row {
                                Text(text = tempoTotalString.value, fontSize = 8.sp )
                                Text("/", fontSize = 8.sp )
                                Text(text =duracaoString.value, fontSize = 8.sp )

                            }
                            Slider(
                                value = duracao.value,
                                onValueChange = {
                                    scope.launch {
                                        val posicao =(it*tempoTotal.value)/100f
                                        vm.seekTo(posicao.toLong())
                                    }
                                },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(activeTrackColor = DarkPink),
                                modifier = Modifier.height(if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 5.dp else 10.dp)
                            )
                            Spacer(Modifier.padding(3.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                            ) {
                                IconButton({scope.launch { vm.preview() }}) {
                                Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = DarkPink)
                            }
                            IconButton({
                                scope.launch {
                                    vm.setModoAleatorio(!modoAleatorio.value)}
                            }) {
                                if(!modoAleatorio.value)
                                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = DarkPink)
                                else
                                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24), contentDescription = null,tint = DarkPink)
                            }

                            IconButton({
                                scope.launch {
                                    if(reproduzindo.value)
                                        vm.pause()
                                    else vm.play()
                                }

                            }) {
                                if (reproduzindo.value)
                                    Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                                        contentDescription = null,
                                        tint = DarkPink,
                                        modifier = Modifier)
                                else
                                    Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                        contentDescription = null,
                                        tint = DarkPink,
                                        modifier = Modifier)
                            }

                            IconButton({
                                scope.launch {
                                    when(modoRepeticao.value){
                                        is ModoDerepeticao.Desativado->vm.setModoDeRepeticao(ModoDerepeticao.RepetirEssa)
                                        is ModoDerepeticao.RepetirEssa->vm.setModoDeRepeticao(ModoDerepeticao.RepetirTodos)
                                        is ModoDerepeticao.RepetirTodos->vm.setModoDeRepeticao(ModoDerepeticao.Desativado)
                                    }
                                }


                            }) {
                                when(modoRepeticao.value){
                                    is ModoDerepeticao.Desativado->Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = DarkPink)
                                    is ModoDerepeticao.RepetirEssa->Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24), contentDescription = null,tint = DarkPink)
                                    is ModoDerepeticao.RepetirTodos->Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24), contentDescription = null,tint = DarkPink)
                                }

                            }
                            IconButton({vm.next()}) {
                                Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                    contentDescription = null,
                                    tint = DarkPink,modifier = Modifier)

                            }

                        }
                    }
                            }
                        }


                    }





            Spacer(Modifier.padding(10.dp))
            Column(
                Modifier

                    .clip(RoundedCornerShape(15.dp))
                    .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Playlist", fontFamily = FontFamily.Monospace)
                Box {
                    LazyColumn(
                        Modifier
                            .align(Alignment.TopCenter)
                            ) {
                        itemsIndexed(items = plyListAtual.value) {indice,item->
                            if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                    Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, tint = DarkPink)
                                    ItemDaLista(modifier = Modifier.clickable{
                                        vm.seekToItem(indice)
                                    },item = item)
                                }
                            else
                            ItemDaLista(modifier = Modifier.clickable{
                                vm.seekToItem(indice)
                            },item = item)
                        }

                    }


                }
            }
        }

        }









@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun PlayerPreview(){
    SongsTheme {
        Surface {
        Scaffold(topBar = { BarraSuperio(titulo = "Plyer") }, modifier = Modifier
            .safeDrawingPadding()
            .safeGesturesPadding()
            .safeContentPadding()) {

            val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
            val context= LocalContext.current
           BigPlayer(modifier = Modifier.padding(it),windowSizeClass = windowsizeclass,paddingValues = it,vm = VmodelPlayer(
                MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado) ),
                acaoAvisoBigplyer = {},
                vmlista = ViewModelListas(repositorio = RepositorioService(context), estado =  MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)),
                acaoDeVoutar = { })

        }
        }
    }

    }


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun PreviaPlyer2(){
    SongsTheme {
        Surface {
            Scaffold(
                Modifier
                    .safeDrawingPadding()
                    .safeGesturesPadding()
                    .safeContentPadding()) {
            /*Box(modifier = Modifier.padding(it))  {
              PlayerCompat(vm = VmodelPlayer(MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)))
             }*/
            }
            }

    }
}