package com.example.songs.componentes.paineis

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
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.Miniplayer
import com.example.songs.componentes.MiniplayerParaTransicao
import com.example.songs.componentes.getMetaData
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme
import com.example.songs.viewModels.PlyerRepeticao
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/*
* BigPlyer representa o player em si aonde se pode ver os dados da musica em reproducao no momento
* e responsavel por medir e determinar como deve ser esibido em cado tamanho de tela
* */

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun BigPlayer(vm: VmodelPlayer, modifier: Modifier = Modifier, windowSizeClass: WindowSizeClass, paddingValues: PaddingValues){
    when{
      windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT->{
          PlayerCompat2(modifier=Modifier, vm = vm)
      }
      windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM->{
          when(windowSizeClass.windowHeightSizeClass){
              WindowHeightSizeClass.COMPACT->{
                  PlyerEspandido(modifier=Modifier,
                                 windowSizeClass = windowSizeClass,
                                 vm = vm)
              }
              WindowHeightSizeClass.MEDIUM->{
                  PlyerEspandido(modifier =Modifier,
                                 windowSizeClass=windowSizeClass,
                                 vm=vm)
              }
              WindowHeightSizeClass.EXPANDED->PlayerCompat2(modifier=Modifier,
                                                            vm = vm)

          }

      }
      windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED->{
          PlyerEspandido(windowSizeClass = windowSizeClass,
                         modifier=Modifier,
                         vm = vm)
      }



    }



   /* val  navegacao = rememberListDetailPaneScaffoldNavigator<Long>( )
  if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
      PlayerCompat2(modifier=Modifier)

 else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) PlayerCompat2()
    else  PlyerEspandido(modifier,windowSizeClass)*/





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Player(modifier: Modifier=Modifier){
    Column(modifier =modifier.padding(10.dp)
                             .background(color = MaterialTheme.colorScheme.background)) {

        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),
             modifier = Modifier.size(400.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .border(width = 0.5.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(15.dp)),
              contentDescription = null,
              tint = DarkPink)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier) {

            Text(text = "Nome da Musica")
            Spacer(Modifier.padding( 8.dp))
            Text(text = "Nome do Artista")
            Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.width(400.dp)) {
            var range= remember {  mutableStateOf<Float>(0f)}
             Slider(value = range.value,
                    onValueChange = {range.value=it},
                    colors = SliderDefaults.colors(activeTrackColor = DarkPink), valueRange = 0f..1f)
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
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlyerParaTransicao(modifier: Modifier=Modifier,sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope: AnimatedVisibilityScope,vm: VmodelPlayer){
    var range= remember {  mutableStateOf<Float>(0f)}
    val duracao=vm._duracao.collectAsState(0L)
    val reproducao=vm._emreproducao.collectAsState(false)
    val metadata=vm._metadataAtual.collectAsState()
    val tempoTotal=vm._tempoTotal.collectAsState(0L)
    val bitmap=remember{ mutableStateOf<Bitmap?>(null)}
    val repet=vm._repeticao.collectAsState(PlyerRepeticao.NaoRepetir)
    val aleatorio=vm._aleatorio.collectAsState()
    val scop=rememberCoroutineScope()
    val context=LocalContext.current
    LaunchedEffect(key1 = metadata.value?.mediaMetadata?.artworkUri){
        scop.launch(Dispatchers.IO) {
            try {
               bitmap.value= getMetaData(metadata.value!!.mediaMetadata.artworkUri!!,metadata.value!!.mediaId!!.toLong(),context,"bigplayer")
            }catch (e:Exception){
                bitmap.value=null
            }
        }
    }
    val stateRange = remember { derivedStateOf { range.value } }
    with(sharedTransitionScope){
        Column(modifier =modifier.padding(10.dp)
                                 .background(color = MaterialTheme.colorScheme.background),
                                             horizontalAlignment = Alignment.CenterHorizontally,
                                             verticalArrangement = Arrangement.Center) {
            if(bitmap.value==null)
            Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),
                 contentDescription = null,
                 tint = DarkPink,
                 modifier = Modifier.size(300.dp)
                                   .clip( RoundedCornerShape(15.dp))
                                   .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                                   .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope),)

            else{
                val bit=bitmap.value!!.asImageBitmap()
                Image(bitmap = bit,
                      contentDescription = null,
                      modifier = Modifier.size(300.dp)
                                         .clip( RoundedCornerShape(15.dp))
                                         .sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),animatedVisibilityScope),)
            }

            Spacer(Modifier.padding(10.dp))
            Column(modifier = Modifier) {

                Text(text = if(metadata.value==null)"Nome da Musica" else metadata.value!!.mediaMetadata.title.toString() ,
                     modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope))
                Spacer(Modifier.padding( 8.dp))
                Text(text = if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString() ,
                     modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))
                Spacer(Modifier.padding(10.dp))
                Column(modifier = Modifier.width(400.dp)) {
                    val rangeValue:()->Float ={
                       duracao.value as Float

                    }
                    Slider(value =duracao.value.toFloat(),
                           onValueChange = {
                       scop.launch {
                           vm.seekTo(it)}},
                          colors = SliderDefaults.colors(activeTrackColor = DarkPink),
                          valueRange = 0f..100f,
                          modifier = Modifier.height(10.dp))
                    Spacer(Modifier.padding(10.dp))

                    Row (modifier = Modifier.fillMaxWidth(),
                         horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                      IconButton(onClick = {
                          scop.launch { vm.preview() }}) {
                          Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                               contentDescription = null,
                               tint = DarkPink)}
                        IconButton(onClick = {
                            scop.launch {
                                 vm.aleatorio(!aleatorio.value)
                               }}) {
                            if (aleatorio.value)
                                Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24),
                                    contentDescription = null,
                                    tint = DarkPink)
                            else
                            Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24),
                                 contentDescription = null,
                                 tint = DarkPink)}

                        IconButton(onClick = {
                            scop.launch { if(reproducao.value) vm.pause() else vm.play() }}) {
                            if(reproducao.value)
                                Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                                    contentDescription = null,
                                    tint = DarkPink,
                                    modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope))
                             else
                            Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                contentDescription = null,
                                tint = DarkPink,
                                modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.PlayeBtn.label),animatedVisibilityScope))
                        }
                        IconButton(onClick = {
                            scop.launch {
                                when(val r=repet.value){
                                        is PlyerRepeticao.NaoRepetir->{
                                            vm.repeticao(PlyerRepeticao.RepetirUm)
                                        }
                                    is PlyerRepeticao.RepetirUm->{
                                        vm.repeticao(PlyerRepeticao.RepetirTodos)
                                    }
                                    is PlyerRepeticao.RepetirTodos->{
                                        vm.repeticao(PlyerRepeticao.NaoRepetir)
                                    }

                            }
                                }
                        }) {
                            when(repet.value){
                                is PlyerRepeticao.NaoRepetir->
                                    Icon(painter = painterResource(id = R.drawable.baseline_repeat_24),
                                    contentDescription = null,
                                    tint = DarkPink)
                                is PlyerRepeticao.RepetirUm->
                                    Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24),
                                        contentDescription = null,
                                        tint = DarkPink)
                                is PlyerRepeticao.RepetirTodos->
                                    Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24),
                                        contentDescription = null,
                                        tint = DarkPink)

                            }
                        }

                        IconButton(onClick = {
                            scop.launch {
                                vm.next()
                            }}) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                 contentDescription = null,
                                 tint = DarkPink,
                                 modifier = Modifier.sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NextPlyer.label),animatedVisibilityScope)) }


                    }
                }
            }


        }
    }
}


@Composable
fun PlyerComtrasicaoLayt(modifier: Modifier=Modifier,){}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerCompat(modifier: Modifier=Modifier,sharedTransitionScope: SharedTransitionScope,animatedVisibilityScope:AnimatedVisibilityScope,onclick:()->Unit={},vm: VmodelPlayer ){
    val listaAvberta=remember{ mutableStateOf(false)}
      with(sharedTransitionScope){
          Box(modifier = modifier.fillMaxSize()
                                .imePadding()
                                .sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),animatedVisibilityScope,
                                                                         resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)){
              PlyerParaTransicao(modifier = Modifier.align( Alignment.TopCenter),
                                 animatedVisibilityScope = animatedVisibilityScope,
                                 sharedTransitionScope = sharedTransitionScope,vm=vm)


              IconButton({
                  listaAvberta.value=!listaAvberta.value
                  onclick()},modifier = Modifier.align(Alignment.BottomCenter).size(70.dp).padding(5.dp)) {
                  Icon(painter = painterResource(id = R.drawable.baseline_list_24),
                       contentDescription = null,
                       tint = DarkPink,
                       modifier = Modifier.align(Alignment.BottomCenter)
                                          .size(50.dp)
                                          .padding(5.dp))

              }


          }
      }


}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PlayerCompat2(vm: VmodelPlayer,modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass=currentWindowAdaptiveInfo().windowSizeClass) {
    val listaAvberta = remember { mutableStateOf(false) }
    val slidervalue = remember { mutableStateOf(0f) }

  if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.EXPANDED) Log.d("classe de tela","espandida")
 else if (windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.COMPACT) Log.d("classe de tela","compacta")
 else Log.d("classe de tela","medium")

    Box(modifier = Modifier.fillMaxSize()) {

        SharedTransitionLayout {
            AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->

                Box(modifier = modifier.fillMaxSize()
                                       .imePadding()) {
                    SharedTransitionLayout {
                        AnimatedContent(targetState = listaAvberta.value) { targetState: Boolean ->
                            if (!targetState) {
                                PlayerCompat(sharedTransitionScope = this@SharedTransitionLayout,
                                             animatedVisibilityScope = this@AnimatedContent,
                                             onclick = {listaAvberta.value=!listaAvberta.value},
                                             vm = vm)

                            } else {
                                Column(modifier = Modifier.sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),
                                                                                     this@AnimatedContent,
                                                                                                   resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)) {
                                    MiniplayerParaTransicao(sharedTransitionScope = this@SharedTransitionLayout,
                                                            animatedVisibilityScope = this@AnimatedContent,
                                                            vm = vm)
                                    Spacer(Modifier.padding(10.dp))
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement =Arrangement.Center){
                                        IconButton(onClick = {
                                            listaAvberta.value=!listaAvberta.value},
                                                  modifier = Modifier.size(60.dp)) {
                                        Icon(Icons.Default.ArrowDropDown,
                                             contentDescription = null,
                                             tint = DarkPink,
                                             modifier=Modifier.size(50.dp))
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
/*
* fass a marccacao dos items compartilhados entre as transicoes dos layouts do player
* e plylist
* */
sealed class ComponetesCompartilhados(val label:String){
    object PlayeBtn:ComponetesCompartilhados(label = "PlayeBtn")
    object NextPlyer:ComponetesCompartilhados(label = "NextPlyer")
    object ImagemEIcones:ComponetesCompartilhados(label = "Imagem/Icones")
    object NomeDaMusica:ComponetesCompartilhados(label = "NomeDaMusica")
    object NomeDoArtista:ComponetesCompartilhados(label = "NomeDoArtista")

}
/*
* fass marcacao entre os layouts do player e playlist
* */
sealed class LayoutsCompartilhados(val label:String){
    object LayoutPluer:LayoutsCompartilhados(label = "LayoutPluer")
}








    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
fun PlyerEspandido(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass,vm: VmodelPlayer) {
        val progresso = remember { mutableStateOf<Float>(0f) }
        Row(modifier.fillMaxSize(), verticalAlignment = Alignment.Top) {
           PlayerPlyerSpandido(modifier=Modifier.align(Alignment.Top),
                               windowSizeClass=windowSizeClass,progresso,
                               vm = vm)
            Spacer(Modifier.padding(10.dp))
            PlayListPlyerSpandido(modifier=Modifier.align(Alignment.Top))


        }


    }

@Composable
fun PlayListPlyerSpandido(modifier: Modifier=Modifier){
    Column(
        modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
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


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PlayerPlyerSpandido(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass,progresso:MutableState<Float>,vm: VmodelPlayer){
    Box(modifier.fillMaxWidth(0.5f).clip(RoundedCornerShape(15.dp))) {

    Column(Modifier.align(Alignment.TopCenter)) {
        val iconeSize =
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 120.dp else 400.dp
        val emreproducao =vm._emreproducao.collectAsState()
        val metadata=vm._metadataAtual.collectAsState()
        val tempoTotal=vm._tempoTotal.collectAsState()
        val duracao=vm._duracao.collectAsState()
        val scope= rememberCoroutineScope()
        val bitmap=remember{ mutableStateOf<Bitmap?>(null)}
        val context= LocalContext.current
       LaunchedEffect(key1 = metadata.value) {
         scope.launch {
             try {
                 bitmap.value= getMetaData(metadata!!.value!!.mediaMetadata!!.artworkUri!!,metadata!!.value!!.mediaId.toLong(),context)

             }catch (e:Exception){
                 Log.d("tentando metadata","essecao ")
                 bitmap.value=null

             }
         }

       }
        if (bitmap.value==null)
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
        else{
            val bit=bitmap.value!!.asImageBitmap()
            Image(
                bitmap = bit,
                contentDescription = null,
                modifier = Modifier.size(iconeSize).clip(
                    RoundedCornerShape(15.dp)
                )
                   .align(Alignment.CenterHorizontally)
            )
        }

        Column {

            Text(text = if(metadata.value==null) "Nome da Musica" else metadata.value!!.mediaMetadata!!.title.toString(),
                 fontSize = 18.sp)
            Spacer(Modifier.padding(1.dp))
            Text(text =if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata!!.artist.toString(),)
            Spacer(Modifier.padding(1.dp))
            Spacer(Modifier.padding(1.dp))
            Column {
                Slider(
                    value = duracao.value.toFloat(),
                    onValueChange = { scope.launch { vm.seekTo(it) } },
                    valueRange = 0f..100f ,
                    colors = SliderDefaults.colors(activeTrackColor = DarkPink),
                    modifier = Modifier.fillMaxWidth().height(10.dp)
                )
                Spacer(Modifier.padding(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                   IconButton(onClick = {
                       scope.launch {
                           vm.preview()
                       }
                   }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                        contentDescription = null,
                        tint = DarkPink,
                        modifier = Modifier.size(20.dp)
                    )}
                IconButton(onClick = {scope.launch {  }}) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_shuffle_24),
                        contentDescription = null,
                        tint = DarkPink,
                        modifier = Modifier.size(20.dp)
                    )
                }
                 IconButton(onClick = {scope.launch {
                     if(emreproducao.value) scope.launch { vm.pause()}
                     else scope.launch { vm.play()}
                 }}) {
                     if (emreproducao.value)
                     Icon(
                         painter = painterResource(id = R.drawable.baseline_pause_24),
                         contentDescription = null,
                         tint = DarkPink,
                         modifier = Modifier.size(20.dp)
                     )
                    else
                         Icon(
                             painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                             contentDescription = null,
                             tint = DarkPink,
                             modifier = Modifier.size(20.dp)
                         )
                 }
                    IconButton(onClick = {scope.launch {  }}) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_repeat_24),
                            contentDescription = null,
                            tint = DarkPink,
                            modifier = Modifier.size(20.dp)
                        )



                    }
                    IconButton(onClick = {
                        scope.launch {
                            vm.next()
                        }
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null,
                        tint = DarkPink,
                        modifier = Modifier.size(20.dp)
                    )}

                }
            }


        }

    }


}}




@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun PlayerPreview(){
    SongsTheme {
        Surface {
        Scaffold(topBar = { BarraSuperio(titulo = "Plyer") }, modifier = Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding()) {

            val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
            BigPlayer(modifier = Modifier.padding(it),
                      windowSizeClass = windowsizeclass,
                      paddingValues = it,
                      vm = VmodelPlayer(
                MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)
            ))

        }
        }
    }

    }


@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun PreviaPlyer2(){
    SongsTheme {
        Surface {
            Scaffold(Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding()) {
             Box(modifier = Modifier.padding(it))  {
              PlayerCompat2(vm = VmodelPlayer(MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)))
             }
            }
            }

    }
}