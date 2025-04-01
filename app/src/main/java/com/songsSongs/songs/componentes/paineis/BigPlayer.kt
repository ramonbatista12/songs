package com.songsSongs.songs.componentes.paineis

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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.palette.graphics.Palette
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.AuxiliarMudancaDeBackGrands
import com.songsSongs.songs.componentes.ItemDaLista

import com.songsSongs.songs.componentes.MedicoesPlyer
import com.songsSongs.songs.componentes.MiniplayerParaTransicao
import com.songsSongs.songs.componentes.MovimentoRetorno
import com.songsSongs.songs.componentes.getMetaData
import com.songsSongs.songs.repositorio.RepositorioService
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.songsSongs.songs.ui.theme.DarkPink
import com.songsSongs.songs.ui.theme.SongsTheme
import com.songsSongs.songs.viewModels.ImagemPlyer
import com.songsSongs.songs.viewModels.ModoDerepeticao
import com.songsSongs.songs.viewModels.ViewModelListas
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
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
              acaoAvisoBigplyer:()->Unit,
              acaoDeVoutar: () -> Unit,
              acaMudarBackgraudScafolld:(Color)->Unit={},
              acaoMudarCorScafollEBArraPermanente:(backgrand:Color,corBarra:Color)->Unit={b,c->},
              acaoOcultarBaras:()->Unit={},
              acaoMostraBaras:()->Unit={}){
    val context= LocalView.current
  val scop = rememberCoroutineScope()
   val acaodevoutar= remember{ mutableStateOf(false)}
   val decompostomposto= rememberSaveable { mutableStateOf(false) }

   LaunchedEffect(Unit) {
       //acaoOcultarBaras()
       acaodevoutar.value=false
       if(decompostomposto.value){
        acaoAvisoBigplyer()
       decompostomposto.value=false
       }
   }
    DisposableEffect(Unit) {

        onDispose {
            if(!acaodevoutar.value) {
               // acaoMostraBaras()
                acaoAvisoBigplyer()
                decompostomposto.value=true

            }




        }
    }

SelecaoDosPlyer(modifier,
                windowSizeClass,
                paddingValues,
                vm,
                vmlista,
                acaoAvisoBigplyer,
                acaoDeVoutar={
                    acaodevoutar.value=true
                    scop.launch {
                        acaoMostraBaras()
                        delay(100)
                        acaMudarBackgraudScafolld(Color.Unspecified)
                        delay(100)
                        acaoAvisoBigplyer()
                        delay(100)
                        acaoDeVoutar()


                    }
                  //  acaodevoutarPreditivo.value=trueacaoDeVoutar()

                    },
                acaMudarBackgraudScafolld=acaMudarBackgraudScafolld,
                acaoMudarCorScafollEBArraPermanente=acaoMudarCorScafollEBArraPermanente,
                acaoMudarScala={x,y,ofx,ofy->

                })




}


@Composable
fun SelecaoDosPlyer(modifier: Modifier = Modifier,
                    windowSizeClass: WindowSizeClass,
                    paddingValues: PaddingValues,
                    vm: VmodelPlayer,vmlista: ViewModelListas,
                    acaoAvisoBigplyer:()->Unit,
                    acaoDeVoutar: () -> Unit={},
                    acaMudarBackgraudScafolld: (Color) -> Unit={},
                    acaoMudarCorScafollEBArraPermanente:(backgrand:Color,corBarra:Color)->Unit={b,c->},
                    acaoMudarScala:(x:Float,y:Float,ofsetx:Float,offsety:Float)->Unit={x,y,ofx,ofy->}){
    val cor=MaterialTheme.colorScheme.background
    val corbackgrand= remember { mutableStateOf(cor) }
    val int =MaterialTheme.colorScheme.background.value.toInt()
    val coresBackgrad=remember { mutableStateOf<List<Color>?>(null) }
    if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
        PlayerCompat(modifier=modifier,
                     vm = vm,
                     vmlista = vmlista,
                     acaoDeVoutar=acaoDeVoutar,
                     acaMudarBackgraudScafolld = acaMudarBackgraudScafolld,
                     acaoMudarEscala = acaoMudarScala )

    else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM)
        if(windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.MEDIUM ||windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.EXPANDED)
            PlayerCompat(modifier,
                         vm = vm,
                         vmlista = vmlista,
                         acaoDeVoutar=acaoDeVoutar,
                         acaMudarBackgraudScafolld = acaMudarBackgraudScafolld, acaoMudarEscala = acaoMudarScala)
        else  PlyerEspandido(modifier,windowSizeClass,vm=vm,vmlista,corbackgrand,acaoMudarCorScafollEBArraPermanente,acaoMudarScala,acaoDeVoutar)
    else  PlyerEspandido(modifier,windowSizeClass,vm=vm,vmlista,corbackgrand,acaoMudarCorScafollEBArraPermanente,acaoMudarScala,acaoDeVoutar)
}



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Plyer(modifier: Modifier=Modifier,
          sharedTransitionScope: SharedTransitionScope,
          animatedVisibilityScope: AnimatedVisibilityScope,
          vm:VmodelPlayer,
          acaoMudarBackgraud:suspend (bitmap:Bitmap?)->Unit={},
          cor:Color=MaterialTheme.colorScheme.onBackground,
          acaoDesaidaDoplyer:()->Unit={}){


    val mediaItem=vm._mediaItemAtual.collectAsState()
    val tempoTotal=vm._tempoTotal.collectAsState()
    val reproduzindo=vm._emreproducao .collectAsState()
    val modoAleatorio=vm._modoAleatorio.collectAsState()
    val modoRepeticao=vm._modoRepeticao.collectAsState()
    val duracao=vm._duracao.collectAsState()
    val duracaoString=vm._tempoTotalString.collectAsState()
    val tempoTotalString=vm._duracaoString.collectAsState()
    val imagem =vm._imagemPlyer.collectAsState()
    val context= LocalContext.current
    val scop=rememberCoroutineScope()
    val caregando =vm._caregando.collectAsState()
    val medicoes=remember { MedicoesPlyer() }

    LaunchedEffect(mediaItem.value){

        vm.caregarImagePlyer({ uri,id->
            try {
               val bitMap:Bitmap?= getMetaData(context = context, uri = uri, id = id, height = 400, whidt =  400)
                acaoMudarBackgraud(bitMap)
                bitMap
            }catch (e:Exception){
                null
            }

        },uri = mediaItem.value!!.mediaMetadata.artworkUri!!,id = mediaItem!!.value!!.mediaId.toLong())




    }
    DisposableEffect(Unit){
        onDispose {
            acaoDesaidaDoplyer()

            scop.cancel()
        }
    }
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
val iconsize=medicoes.larguraImagemPlyerCompoat(windowSizeClass)
    with(sharedTransitionScope){
        Column(modifier = modifier.onSizeChanged {

            Log.e("largura","largura , altura ${it.width} , ${it.height}")
        }
            .padding(10.dp)
            .background(color =Color.Transparent),
               horizontalAlignment = Alignment.CenterHorizontally) {
           when(val r=imagem.value){
               is ImagemPlyer.Vazia->Icon(painter = painterResource(id =r.icone),
                                          contentDescription = null,
                                          tint = DarkPink,
                                          modifier = Modifier//.size(iconSize.dp)
                                                             .fillMaxWidth(iconsize)
                                                             .aspectRatio(1f)
                                                             .clip(RoundedCornerShape(15.dp))
                                                             .border(width = 1.5.dp, color = cor, shape = RoundedCornerShape(15.dp))
                                                             .sharedElement(
                                                                          rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),
                                                                                                     animatedVisibilityScope),)
               is ImagemPlyer.Imagem->{
                   val _bitmap=r.imagem.asImageBitmap()
                   Image(bitmap=_bitmap,
                         contentDescription = null,
                         modifier = Modifier//.size(250.dp).fillMaxHeight(0.4f) 0.7
                                           .fillMaxWidth(iconsize)
                                           .aspectRatio(1f)
                                           .clip(RoundedCornerShape(15.dp))
                                           .sharedElement( rememberSharedContentState(key = ComponetesCompartilhados.ImagemEIcones.label),
                                                           animatedVisibilityScope),
                         contentScale = ContentScale.FillBounds)}
           }



            Spacer(Modifier.padding(10.dp))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

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

                   Box(modifier=Modifier.fillMaxWidth().fillMaxHeight(0.10f)) {

                         Text(text = tempoTotalString.value,
                             color = cor, fontSize = 8.sp, modifier = Modifier.align(Alignment.BottomStart) )
                        // Text("/", fontSize = 8.sp, color = cor )
                       androidx.compose.animation.AnimatedVisibility(visible = !caregando.value,modifier = Modifier.align(Alignment.BottomEnd)){
                           Text(text =duracaoString.value, fontSize = 8.sp, color = cor, modifier = Modifier.align(Alignment.BottomEnd)  )
                       }
                        androidx.compose.animation.AnimatedVisibility(visible = caregando.value,modifier = Modifier.align(Alignment.BottomEnd)) {
                            CircularProgressIndicator(modifier = Modifier.size(10.dp).align(Alignment.BottomEnd),color = cor)
                        }
                        }

                    val interacao=remember { MutableInteractionSource() }
                    Slider(value = duracao.value, onValueChange = {
                        scop.launch {
                            val valor=(it*tempoTotal.value)/100f
                            vm.seekTo(valor.toLong())
                        }
                    },
                         colors = SliderDefaults.colors(activeTrackColor = cor),
                        thumb = {SliderDefaults.Thumb(modifier = Modifier.size(20.dp),interactionSource = interacao, colors = SliderDefaults.colors(thumbColor = cor))
                        },
                        track = {SliderDefaults.Track(it,modifier=Modifier.height(5.dp),colors = SliderDefaults.colors(activeTrackColor = cor))},
                         valueRange = 0f..100f,
                         modifier = Modifier.fillMaxHeight(0.20f))
                    Spacer(Modifier.padding(10.dp))

                    Row (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.40f),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
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
                     acaoMudarBackgraud:suspend (bitmap:Bitmap?)->Unit={},
                     acoDesaidaDoPlyer:()->Unit={},
                     acaoDeVoutar: () -> Unit={}){
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
              IconButton (onClick ={
              Log.i("teste icom butom","acao de voutar foi clicada")
              acaoDeVoutar()
          },modifier = Modifier.size(50.dp)) {
              Icon(painter = painterResource(R.drawable.outline_west_24), contentDescription = null, tint = cor.value, modifier = Modifier)
          }
              Plyer(Modifier.align( Alignment.TopCenter).background(Color.Transparent),
                    animatedVisibilityScope = animatedVisibilityScope,sharedTransitionScope = sharedTransitionScope,
                    vm=vm, acaoMudarBackgraud = acaoMudarBackgraud,cor = cor.value, acaoDesaidaDoplyer = acoDesaidaDoPlyer)


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
                 acaoDeVoutar:()->Unit={},
                 acaMudarBackgraudScafolld: (Color) -> Unit={},
                 acaoMudarEscala:(x:Float,y:Float,ofsetx:Float,offsety:Float)->Unit={x,y,ofx,ofy->}) {
    val listaAvberta = remember { mutableStateOf(false) }
    val backgraudColor =MaterialTheme.colorScheme.background
    val textColorSquemas=MaterialTheme.colorScheme.onBackground
    val cor = remember { mutableStateOf(Color(backgraudColor.value.toInt())) }
    val corTexto=remember { mutableStateOf(Color.Black) }
    val animacaoFuncao=remember { MovimentoRetorno() }
    /* PredictiveBackHandler { progress: Flow<BackEventCompat> ->
        progress.collect { backEvent ->
            try {
                animacaoFuncao.animacao(backEvent.progress,
                                       acaoDeVoutar,
                                       acaoMudarEscala,
                                       acaoMudarCor = {acaMudarBackgraudScafolld(backgraudColor)},
                                       acaoReverterCorbackgrand = {acaMudarBackgraudScafolld(cor.value)})
             // acaMudarBackgraudScafolld(backgraudColor)
            } catch (e: CancellationException) {
                Log.d("progress animacao ","${backEvent.progress} ,camcelado ${e.message}")
               acaMudarBackgraudScafolld(cor.value)
                acaoMudarEscala(0f,0f,0f,0f)
            }


        }
    }*/

        Box(modifier = modifier.fillMaxSize().background(cor.value)) {

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
                                               AuxiliarMudancaDeBackGrands().mudarBackgrandScaffold(it,
                                                                                                    backgraudColor=backgraudColor,
                                                                                                    cor=cor,corTexto=corTexto,
                                                                                                    textColorSquemas=textColorSquemas, acao = acaMudarBackgraudScafolld)
                                    },acoDesaidaDoPlyer = {acaMudarBackgraudScafolld(backgraudColor)},acaoDeVoutar)

                            } else {

                                val estadoPlylist=vmlista._estadoPlylsist.collectAsState()
                                val lista =vmlista.plylist().collectAsState(emptyList()) //

                                Column(Modifier.sharedBounds(rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),this@AnimatedContent, resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds)
                                    .background(backgraudColor)) {
                                  Row (modifier=Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                      MiniplayerParaTransicao(
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this@AnimatedContent,
                                        vm = vm,
                                        corDotexto = corTexto.value,
                                        backgraud = cor.value,
                                        acaoMudarBackgraud = {
                                            AuxiliarMudancaDeBackGrands().mudarBackgrandMiniPlyer(it,
                                                                                                  backgraudColor=cor,
                                                                                                  corTexto = corTexto,
                                                                                                  textColorSquemas = textColorSquemas,
                                                                                                  backgraudColorSquemas = backgraudColor)
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
                                    val indice=vm._indice.collectAsState()
                                    val listState= rememberLazyListState(initialFirstVisibleItemIndex = indice.value)

                                    LazyColumn(state =  listState,modifier=Modifier.background(color = backgraudColor).fillMaxWidth().fillMaxHeight()) {

                                        itemsIndexed(items = lista.value) {indice,item->
                                           if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                                               Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                                   Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, )
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
                            DisposableEffect(Unit) {
                                onDispose {
                                    acaMudarBackgraudScafolld(cor.value)
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








    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("SuspiciousIndentation")
    @Composable
fun PlyerEspandido(modifier: Modifier=Modifier,
                   windowSizeClass: WindowSizeClass,
                   vm:VmodelPlayer,vmlista:ViewModelListas,
                   cor:MutableState<Color>,
                   acaoMudarCorScafollEBArraPermanente: (backgrand: Color, corBarra: Color) -> Unit={b,c->},
                   acaoMudarEscala:(x:Float,y:Float,ofsetx:Float,offsety:Float)->Unit={x,y,ofx,ofy->},
                   acaoDeVoutar: () -> Unit={}) {
       val metadata=vm._mediaItemAtual.collectAsState()
       val plyListAtual=vmlista.plylist().collectAsState(emptyList())
       val backgraudColor=MaterialTheme.colorScheme.background
       val textColorSquemas=MaterialTheme.colorScheme.onBackground
       val auxiliar= remember { MovimentoRetorno() }
       val corTexto=remember { mutableStateOf(textColorSquemas) }
       val indice =vm._indice.collectAsState()
        DisposableEffect(Unit) {
            onDispose {
                acaoMudarCorScafollEBArraPermanente(backgraudColor,textColorSquemas)
            }
        }
       /* PredictiveBackHandler { progress: Flow<BackEventCompat> ->
            progress.collect { backEvent ->
                try {
                   auxiliar.animacao(backEvent.progress,
                            acaoDeVoutar,
                           acaoMudarCor = {acaoMudarCorScafollEBArraPermanente(backgraudColor,textColorSquemas)},
                           acaoReverterCorbackgrand = {acaoMudarCorScafollEBArraPermanente(cor.value,corTexto.value)},
                           acaoMudarEscala = acaoMudarEscala
                   )
                    // acaMudarBackgraudScafolld(backgraudColor)
                } catch (e: CancellationException) {
                    Log.d("progress animacao ","${backEvent.progress} ,camcelado ${e.message}")
                    acaoMudarCorScafollEBArraPermanente(cor.value,corTexto.value)
                    acaoMudarEscala(0f,0f,0f,0f)
                }


            }
        }*/


        Row(
            modifier.background(cor.value)
                .fillMaxSize()
                .padding(bottom = 10.dp), verticalAlignment = Alignment.Top) {


           PlyerComtrolerPlyerExtendidi(modifier=Modifier.background(cor.value),vm = vm,
                                        windowSizeClass = windowSizeClass,
                                        metadata = metadata,
                                        acaoMudarBackgraud = {
                                          AuxiliarMudancaDeBackGrands().mudaBackgraundScafolldPermanentBar(it,
                                                                                                           acaoMudarCorScafollEBArraPermanente,
                                                                                                           corBackGraund = cor,
                                                                                                           corTexto=corTexto,
                                                                                                           textColorSquemas = textColorSquemas,
                                                                                                           backgraudColorSquemas = backgraudColor)},acaoDeVoutar=acaoDeVoutar,
        corDotexto = corTexto.value)
           Spacer(Modifier.padding(10.dp))
            Column(
                Modifier.clip(RoundedCornerShape(15.dp)).background(cor.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Playlist", fontFamily = FontFamily.Monospace, color = corTexto.value)
                Box {
                    val listState = rememberLazyListState(initialFirstVisibleItemIndex = indice.value)

                    LazyColumn(state = listState,
                       modifier =  Modifier
                            .align(Alignment.TopCenter)

                            ) {
                        itemsIndexed(items = plyListAtual.value) {indice,item->
                            if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                    Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, tint = corTexto.value)
                                    ItemDaLista(modifier = Modifier.clickable{
                                        vm.seekToItem(indice)
                                    },item = item, cor = corTexto.value)
                                }
                            else
                            ItemDaLista(modifier = Modifier.clickable{
                                vm.seekToItem(indice)
                            },item = item, cor = corTexto.value)
                        }

                    }


                }
            }
        }

        }



@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlyerComtrolerPlyerExtendidi(modifier: Modifier,
                                 vm:VmodelPlayer,
                                 windowSizeClass: WindowSizeClass,metadata: State<MediaItem?>,
                                 acaoMudarBackgraud:suspend (bitmap:Bitmap?)->Unit={},
                                 acaoDeVoutar: () -> Unit={},
                                 corDotexto:Color=MaterialTheme.colorScheme.onBackground){

    val scope= rememberCoroutineScope()
    val context= LocalContext.current
    val duracao=vm._duracao.collectAsState()
    val tempoTotal=vm._tempoTotal.collectAsState()
    val duracaoString=vm._duracaoString.collectAsState()
    val tempoTotalString=vm._tempoTotalString.collectAsState()
    val modoAleatorio=vm._modoAleatorio.collectAsState()
    val  modoRepeticao=vm._modoRepeticao.collectAsState()
    val  reproduzindo=vm._emreproducao.collectAsState()
    val  caregando =vm._caregando.collectAsState(false)
    val imagem=vm._imagemPlyer.collectAsState()
    val medicoes =remember { MedicoesPlyer() }

    LaunchedEffect(metadata.value) {
        vm.caregarImagePlyer({uri, id ->
            try {
                val  bitmap = getMetaData(context = context, uri = uri, id = id, whidt = 400, height = 400)
                acaoMudarBackgraud(bitmap)
                bitmap
            }catch (e:Exception){
                null}

        },uri = metadata.value!!.mediaMetadata.artworkUri!!,id = metadata.value!!.mediaId.toLong())


    }
    DisposableEffect(Unit) {
        onDispose {

            scope.cancel()
        }
    }
    Box(
        modifier = modifier.fillMaxWidth(0.4f)
                           .fillMaxHeight(1f)
                           .padding(10.dp)) {
        IconButton (onClick ={
            Log.i("teste icom butom","acao de voutar foi clicada")
            acaoDeVoutar()
        },modifier = Modifier.align(Alignment.TopStart).size(50.dp)) {
            Icon(painter = painterResource(R.drawable.outline_west_24), contentDescription = null, tint = corDotexto, modifier = Modifier)
        }

        Column(Modifier.align(Alignment.TopCenter)) {
            val iconeSize = medicoes.larguraImagemPlyerEspandido(windowSizeClass)

                //medicoes.tamanhoDoIcone(windowSizeClass)
            when(val r =imagem.value){
                is ImagemPlyer.Vazia->Icon(
                    painter = painterResource(r.icone),
                    contentDescription = null,

                    modifier = Modifier//.size(iconeSize)
                        .fillMaxWidth(iconeSize)
                        .aspectRatio(1f)
                        .clip(
                            RoundedCornerShape(15.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            color = corDotexto,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .align(Alignment.CenterHorizontally), tint = DarkPink
                )
                is ImagemPlyer.Imagem->{ val _bitmap=r.imagem.asImageBitmap()
                    Image(bitmap = _bitmap,
                        contentDescription = null ,
                        modifier = Modifier.fillMaxWidth(iconeSize)
                            .aspectRatio(1f)
                            .clip(
                                RoundedCornerShape(15.dp)
                            )

                            .align(Alignment.CenterHorizontally), contentScale = ContentScale.FillBounds)}
            }

           Spacer(Modifier.padding( all =medicoes.spasamentoImagemTituloPlyerEstendido(windowSizeClass)))
            Column(modifier = Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {

                Text(text = if(metadata.value==null)"Nome da Musica" else metadata.value!!.mediaMetadata.title.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = medicoes.funTSizeTitulo(windowSizeClass),
                    maxLines =medicoes.maxLineTextos(windowSizeClass),
                    color = corDotexto,
                    textAlign = TextAlign.Justify)
                Spacer(Modifier.padding(0.4.dp))
                if(windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT)
                Text(text =if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                    maxLines=medicoes.maxLineTextos(windowSizeClass),
                    fontSize = medicoes.funTSizeSubtitulo(),
                    color = corDotexto,
                    textAlign = TextAlign.Justify)
                Spacer(Modifier.padding(3.dp))
                Column(modifier=Modifier.fillMaxHeight()) {

                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.1f)) {
                        Text(text =duracaoString.value, fontSize = 8.sp, modifier = Modifier.align(Alignment.TopStart), color = corDotexto )
                        //Text("/", fontSize = 8.sp )
                        androidx.compose.animation.AnimatedVisibility(visible =!caregando.value,Modifier.align(Alignment.TopEnd)) {
                            Text(text = tempoTotalString.value, fontSize = 8.sp, modifier = Modifier.align(Alignment.TopEnd), color = corDotexto )
                        }
                        androidx.compose.animation.AnimatedVisibility(visible = caregando.value,Modifier.align(Alignment.TopEnd)) {
                            CircularProgressIndicator(Modifier.size(10.dp).align(Alignment.TopEnd), color = corDotexto)
                        }


                    }
                    val interactionSource = remember { MutableInteractionSource() }
                    Slider(
                        value = duracao.value,
                        onValueChange = {
                            scope.launch {
                                val posicao =(it*tempoTotal.value)/100f
                                vm.seekTo(posicao.toLong())
                            }
                        },
                        valueRange = 0f..100f,
                        thumb = {SliderDefaults.Thumb(interactionSource=interactionSource, modifier = Modifier.size(10.dp), colors = SliderDefaults.colors(thumbColor = corDotexto))},
                        track = {SliderDefaults.Track(it,
                                                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f),
                                                colors =SliderDefaults.colors(activeTrackColor =  corDotexto))},
                        

                        modifier = Modifier.height(if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 5.dp else 10.dp)
                    )
                    Spacer(Modifier.padding(3.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.20f),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        IconButton({scope.launch { vm.preview() }}) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = corDotexto)
                        }
                        IconButton({
                            scope.launch {
                                vm.setModoAleatorio(!modoAleatorio.value)}
                        }) {
                            if(!modoAleatorio.value)
                                Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = corDotexto)
                            else
                                Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24), contentDescription = null,tint = corDotexto)
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
                                    tint = corDotexto,
                                    modifier = Modifier)
                            else
                                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                    contentDescription = null,
                                    tint = corDotexto,
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
                                is ModoDerepeticao.Desativado->Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = corDotexto)
                                is ModoDerepeticao.RepetirEssa->Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24), contentDescription = null,tint = corDotexto)
                                is ModoDerepeticao.RepetirTodos->Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24), contentDescription = null,tint = corDotexto)
                            }

                        }
                        IconButton({vm.next()}) {
                            Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                                contentDescription = null,
                                tint = corDotexto,modifier = Modifier)

                        }

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
        Scaffold(topBar = {  }, modifier = Modifier
            .safeDrawingPadding()
            .safeGesturesPadding()
            .safeContentPadding()) {

            val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
            val context= LocalContext.current
            Box(modifier = Modifier.padding(it).fillMaxSize(1.0f)){
                BigPlayer(modifier = Modifier.align(Alignment.TopCenter),windowSizeClass = windowsizeclass,paddingValues = it,vm = VmodelPlayer(
                    MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado) ),
                    acaoAvisoBigplyer = {},
                    vmlista = ViewModelListas(repositorio = RepositorioService(context), estado =  MutableStateFlow(ResultadosConecaoServiceMedia.Desconectado)),
                    acaoDeVoutar = { })

            }
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