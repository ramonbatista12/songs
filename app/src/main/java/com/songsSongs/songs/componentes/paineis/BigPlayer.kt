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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.palette.graphics.Palette
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.ApresentacaoPlyListPlyerEstendido
import com.songsSongs.songs.componentes.ApresenttacaoDasPlyListsPlyerCompat
import com.songsSongs.songs.componentes.AuxiliarMudancaDeBackGrands
import com.songsSongs.songs.componentes.Banner
import com.songsSongs.songs.componentes.BotoesDeControle
import com.songsSongs.songs.componentes.ImagemPlyer
import com.songsSongs.songs.componentes.IndicadorDeTempo
import com.songsSongs.songs.componentes.ListaPlyLIsts

import com.songsSongs.songs.componentes.MedicoesPlyer
import com.songsSongs.songs.componentes.MovimentoRetorno
import com.songsSongs.songs.componentes.SLidePlyer
import com.songsSongs.songs.componentes.TextoDeApresentacaoDaMusica
import com.songsSongs.songs.componentes.getMetaData
import com.songsSongs.songs.repositorio.RepositorioService
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.songsSongs.songs.ui.theme.SongsTheme
import com.songsSongs.songs.viewModels.ViewModelListas
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.CoroutineScope
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
        acaodevoutar.value=false
        if(decompostomposto.value){
            acaoAvisoBigplyer()
            decompostomposto.value=false
        }
    }

    DisposableEffect(Unit) {

        onDispose {
            if(!acaodevoutar.value) {

                decompostomposto.value=true
                acaoMostraBaras()
                acaoAvisoBigplyer()


            }




        }
    }

SelecaoDosPlyer(modifier,
                windowSizeClass,
                scope = scop,
                vm,
                vmlista,
                acaoAvisoBigplyer,
                acaoDeVoutar={
                    acaodevoutar.value=true
                    scop.launch {
                        acaoMostraBaras()
                       // delay(100)
                        acaMudarBackgraudScafolld(Color.Unspecified)
                        //delay(100)

                        //delay(100)
                        acaoDeVoutar()
                        acaoAvisoBigplyer()

                    }


                    },
                acaMudarBackgraudScafolld=acaMudarBackgraudScafolld,
                acaoMudarCorScafollEBArraPermanente=acaoMudarCorScafollEBArraPermanente,
                )




}


@Composable
fun SelecaoDosPlyer(modifier: Modifier = Modifier,
                    windowSizeClass: WindowSizeClass,
                    scope: CoroutineScope,
                    vm: VmodelPlayer,vmlista: ViewModelListas,
                    acaoAvisoBigplyer:()->Unit,
                    acaoDeVoutar: () -> Unit={},
                    acaMudarBackgraudScafolld: (Color) -> Unit={},
                    acaoMudarCorScafollEBArraPermanente:(backgrand:Color,corBarra:Color)->Unit={b,c->},
                    ){
    val cor=MaterialTheme.colorScheme.background
    val corbackgrand= remember { mutableStateOf(cor) }
    val int =MaterialTheme.colorScheme.background.value.toInt()
    val coresBackgrad=remember { mutableStateOf<List<Color>?>(null) }
    if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
        PlayerCompat(modifier=modifier, scope = scope,
                     vm = vm,
                     vmlista = vmlista,
                     acaoDeVoutar=acaoDeVoutar,
                     acaMudarBackgraudScafolld = acaMudarBackgraudScafolld,
                      )

    else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM)
        if(windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.MEDIUM ||windowSizeClass.windowHeightSizeClass==WindowHeightSizeClass.EXPANDED)
            PlayerCompat(modifier, scope = scope,
                         vm = vm,
                         vmlista = vmlista,
                         acaoDeVoutar=acaoDeVoutar,
                         acaMudarBackgraudScafolld = acaMudarBackgraudScafolld,)
        else  PlyerEspandido(modifier,windowSizeClass,vmodelPlayer=vm,vmlista,corbackgrand,acaoMudarCorScafollEBArraPermanente,acaoDeVoutar)
    else  PlyerEspandido(modifier,windowSizeClass,vmodelPlayer=vm,vmlista,corbackgrand,acaoMudarCorScafollEBArraPermanente,acaoDeVoutar)
}



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Plyer(modifier: Modifier=Modifier,
          sharedTransitionScope: SharedTransitionScope,
          animatedVisibilityScope: AnimatedVisibilityScope,scope: CoroutineScope,
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

        },uri = mediaItem.value!!.mediaMetadata.artworkUri!!,id = mediaItem!!.value!!.mediaId.toLong())}
    DisposableEffect(Unit){
        onDispose {
            acaoDesaidaDoplyer()
           scope.cancel()
        }
    }
val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
val iconsize=medicoes.larguraImagemPlyerCompoat(windowSizeClass)
    with(sharedTransitionScope){
        Column(modifier = modifier.background(color =Color.Transparent).padding(start = 3.dp, end = 3.dp),
               horizontalAlignment = Alignment.CenterHorizontally) {
        ImagemPlyer(imagem,iconsize,this@with,animatedVisibilityScope,cor)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
               TextoDeApresentacaoDaMusica(this@Column,this@with,animatedVisibilityScope,cor,mediaItem)
                Spacer(Modifier.padding(10.dp))
                Column(modifier = Modifier.width(400.dp)) {
                IndicadorDeTempo(this@Column,tempoTotalString,duracaoString,caregando,cor)
                SLidePlyer(duracao,tempoTotal,vm,scope,cor)
                Spacer(Modifier.padding(10.dp))
                BotoesDeControle(vm,scope,this@with,animatedVisibilityScope,modoAleatorio,reproduzindo,modoRepeticao,cor)}
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
                     animatedVisibilityScope:AnimatedVisibilityScope,scope: CoroutineScope,
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
          },modifier = Modifier.size(50.dp).align(Alignment.TopStart)) {
              Icon(painter = painterResource(R.drawable.outline_west_24), contentDescription = null, tint = cor.value, modifier = Modifier)
          }
              Plyer(Modifier.align( Alignment.TopCenter).background(Color.Transparent).padding(top=10.dp),
                    animatedVisibilityScope = animatedVisibilityScope,sharedTransitionScope = sharedTransitionScope,
                    vm=vm, acaoMudarBackgraud = acaoMudarBackgraud,cor = cor.value, acaoDesaidaDoplyer = acoDesaidaDoPlyer,scope = scope)


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
                 scope: CoroutineScope,
                 vm:VmodelPlayer,
                 vmlista:ViewModelListas,
                 acaoMudarLista:(p:Palette)->Unit={},
                 acaoDeVoutar:()->Unit={},
                 acaMudarBackgraudScafolld: (Color) -> Unit={},
                 ) {
    val listaAvberta = remember { mutableStateOf(false) }
    val backgraudColorDefault =MaterialTheme.colorScheme.background
    val textColorSquemas=MaterialTheme.colorScheme.onBackground
    val Backgraud = remember { mutableStateOf(Color(backgraudColorDefault.value.toInt())) }
    val corTexto=remember { mutableStateOf(Color.Black) }



   Box(modifier = modifier.fillMaxSize().background(Backgraud.value)) {

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
                                                                                                    backgraudColor=backgraudColorDefault,
                                                                                                    cor=Backgraud,corTexto=corTexto,
                                                                                                    textColorSquemas=textColorSquemas, acao = acaMudarBackgraudScafolld)
                                    },acoDesaidaDoPlyer = {acaMudarBackgraudScafolld(backgraudColorDefault)}, acaoDeVoutar = acaoDeVoutar,scope = scope)

                            } else {
                                 ApresenttacaoDasPlyListsPlyerCompat(sharedTransitionScope = this@SharedTransitionLayout,
                                                                     animatedVisibilityScope = this@AnimatedContent,
                                                                     scope = scope,
                                                                     backgraudColor=backgraudColorDefault,
                                                                     corDoTexto = corTexto,
                                                                     cor=Backgraud,textColorSquemas=textColorSquemas,
                                                                     vm = vm,
                                                                     vmlista = vmlista,
                                                                     listaAvberta = listaAvberta)
//


                            }
                            DisposableEffect(Unit) {
                                onDispose {
                                    acaMudarBackgraudScafolld(Backgraud.value)
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
                   vmodelPlayer:VmodelPlayer, vmlista:ViewModelListas,
                   bacgrandClor:MutableState<Color>,
                   acaoMudarCorScafollEBArraPermanente: (backgrand: Color, corBarra: Color) -> Unit={b,c->},

                   acaoDeVoutar: () -> Unit={}) {
       val metadata=vmodelPlayer._mediaItemAtual.collectAsState()
       val plyListAtual=vmlista.plylist().collectAsState(emptyList())
       val backgraudColorDefault=MaterialTheme.colorScheme.background
       val textColorSquemas=MaterialTheme.colorScheme.onBackground
       val corTexto=remember { mutableStateOf(textColorSquemas) }
       val indice =vmodelPlayer._indice.collectAsState()
        DisposableEffect(Unit) {
            onDispose {
                acaoMudarCorScafollEBArraPermanente(backgraudColorDefault,textColorSquemas)
            }
        }



        Row(
            modifier.background(bacgrandClor.value)
                .fillMaxSize()
                .padding(bottom = 10.dp), verticalAlignment = Alignment.Top) {


           PlyerComtrolerPlyerExtendidi(modifier=Modifier.background(bacgrandClor.value),vm = vmodelPlayer,
                                        windowSizeClass = windowSizeClass,
                                        metadata = metadata,
                                        acaoMudarBackgraud = {
                                          AuxiliarMudancaDeBackGrands().mudaBackgraundScafolldPermanentBar(it,
                                                                                                           acaoMudarCorScafollEBArraPermanente,
                                                                                                           corBackGraund = bacgrandClor,
                                                                                                           corTexto=corTexto,
                                                                                                           textColorSquemas = textColorSquemas,
                                                                                                           backgraudColorSquemas = backgraudColorDefault)},acaoDeVoutar=acaoDeVoutar,corDotexto = corTexto.value)
           Spacer(Modifier.padding(10.dp))
           ApresentacaoPlyListPlyerEstendido(this@Row,vmodelPlayer,vmlista,bacgrandClor.value, corTexto.value)


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
    val modoAleatorio=vm._modoAleatorio.collectAsState()
    val  modoRepeticao=vm._modoRepeticao.collectAsState()
    val  reproduzindo=vm._emreproducao.collectAsState()
    val imagem=vm._imagemPlyer.collectAsState()
    val medicoes =remember { MedicoesPlyer() }
    val iconeSize = medicoes.larguraImagemPlyerEspandido(windowSizeClass)
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

        Column(Modifier.align(Alignment.TopCenter).fillMaxHeight()) {

            ImagemPlyer(imagem,iconeSize,corDotexto,this@Column)
            Spacer(Modifier.padding( all =medicoes.spasamentoImagemTituloPlyerEstendido(windowSizeClass)))
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(),horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.padding(3.dp))
                Column(modifier=Modifier.fillMaxHeight()) {
                    TextoDeApresentacaoDaMusica(this@Column,medicoes=medicoes,cor=corDotexto,metadata= metadata,windowSizeClass)
                    Spacer(Modifier.padding(5.dp))
                    SLidePlyer(duracao,tempoTotal,vm,scope,corDotexto)
                    Spacer(Modifier.padding(3.dp))
                    BotoesDeControle(scop = scope, vm = vm, columnScope = this@Column, modoAleatorio = modoAleatorio, reproduzindo = reproduzindo, modoRepeticao = modoRepeticao, cor = corDotexto)
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
/*Box {
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
}*/