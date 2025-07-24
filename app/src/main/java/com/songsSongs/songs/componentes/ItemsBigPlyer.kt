package com.songsSongs.songs.componentes

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.Log
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.paineis.ComponetesCompartilhados
import com.songsSongs.songs.componentes.paineis.LayoutsCompartilhados
import com.songsSongs.songs.ui.theme.DarkPink
import com.songsSongs.songs.viewModels.ImagemPlyer
import com.songsSongs.songs.viewModels.ModoDerepeticao
import com.songsSongs.songs.viewModels.ViewModelListas
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TextoDeApresentacaoDaMusica(columnScope: ColumnScope,
                                sharedTransitionScope: SharedTransitionScope,
                                animatedVisibilityScope: AnimatedVisibilityScope,
                                cor: Color, mediaItem: State<MediaItem?>
){
    with(columnScope){
        with(sharedTransitionScope){
            Text(text =if(mediaItem.value==null) "Nome da Musica" else mediaItem.value!!.mediaMetadata.title.toString(),
                modifier = Modifier.basicMarquee(iterations = 10, initialDelayMillis = 1000, repeatDelayMillis = 1000).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDaMusica.label),animatedVisibilityScope),
                color = cor,
                maxLines = 1,
                fontFamily = FontFamily.Monospace)
            Spacer(Modifier.padding( 3.dp))
            Text(text = if (mediaItem.value==null) "Nome do Artista" else mediaItem.value!!.mediaMetadata.artist.toString(), color = cor,
                modifier = Modifier.basicMarquee(iterations = 10, initialDelayMillis = 1000, repeatDelayMillis = 1000).sharedElement(rememberSharedContentState(key = ComponetesCompartilhados.NomeDoArtista.label),animatedVisibilityScope))
        }
    }
}

@Composable
fun TextoDeApresentacaoDaMusica(columnScope: ColumnScope,
                                medicoes: MedicoesPlyer,
                                cor: Color,
                                metadata: State<MediaItem?>, windowSizeClass: WindowSizeClass
){
    with(columnScope){

            Text(text =if(metadata.value==null) "Nome da Musica" else metadata.value!!.mediaMetadata.title.toString(),
                modifier = Modifier.basicMarquee(iterations = 10, initialDelayMillis = 1000, repeatDelayMillis = 1000),
                color = cor,
                maxLines = medicoes.maxLineTextos(windowSizeClass),
                fontFamily = FontFamily.Monospace)
            Spacer(Modifier.padding( 3.dp))
        if(windowSizeClass.windowHeightSizeClass != WindowHeightSizeClass.COMPACT)
            Text(text =if (metadata.value==null) "Nome do Artista" else metadata.value!!.mediaMetadata.artist.toString(),
                maxLines=medicoes.maxLineTextos(windowSizeClass),
                fontSize = medicoes.funTSizeSubtitulo(),
                color = cor,
                textAlign = TextAlign.Justify)
        }
    }

@Composable
fun IndicadorDeTempo(columnScope: ColumnScope, tempoTotalString: State<String>, duracaoString: State<String>, caregando: State<Boolean>, cor: Color){
    with(columnScope){
        Box(modifier= Modifier.fillMaxWidth().fillMaxHeight(0.10f)) {

            Text(text = tempoTotalString.value,
                color = cor, fontSize = 8.sp, modifier = Modifier.align(Alignment.BottomStart) )
            // Text("/", fontSize = 8.sp, color = cor )
            androidx.compose.animation.AnimatedVisibility(visible = !caregando.value,modifier = Modifier.align(
                Alignment.BottomEnd)){
                Text(text =duracaoString.value, fontSize = 8.sp, color = cor, modifier = Modifier.align(
                    Alignment.BottomEnd)  )
            }
            androidx.compose.animation.AnimatedVisibility(visible = caregando.value,modifier = Modifier.align(
                Alignment.BottomEnd)) {
                CircularProgressIndicator(modifier = Modifier.size(10.dp).align(Alignment.BottomEnd),color = cor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SLidePlyer(duracao: State<Float>,
               tempoTotal: State<Long>,
               vm: VmodelPlayer,
                cor: Color
){
    val scope = rememberCoroutineScope()
    val interacao= remember { MutableInteractionSource() }
    Slider(value = duracao.value, onValueChange = {
        scope.launch {
            val valor=(it*tempoTotal.value)/100f
            vm.seekTo(valor.toLong())
        }
    },
        colors = SliderDefaults.colors(activeTrackColor = cor),
        thumb = {
            SliderDefaults.Thumb(modifier = Modifier.size(20.dp),interactionSource = interacao, colors = SliderDefaults.colors(thumbColor = cor))
        },
        track = { SliderDefaults.Track(it,modifier= Modifier.height(5.dp),colors = SliderDefaults.colors(activeTrackColor = cor))},
        valueRange = 0f..100f,
        modifier = Modifier.fillMaxHeight(0.20f))
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BotoesDeControle(vm: VmodelPlayer,
                     sharedTransitionScope: SharedTransitionScope,
                     animatedVisibilityScope: AnimatedVisibilityScope,
                     modoAleatorio: State<Boolean>,
                     reproduzindo: State<Boolean>,
                     modoRepeticao: State<ModoDerepeticao>, cor: Color
){
    val scop= rememberCoroutineScope()
    with(sharedTransitionScope) {
        Row (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.40f),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
            IconButton({scop.launch { vm.preview() }}) {
                Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = cor)
            }
            IconButton({
                scop.launch(Dispatchers.Main) {
                    vm.setModoAleatorio(!modoAleatorio.value)}
            }) {
                if(!modoAleatorio.value)
                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = cor)
                else
                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24), contentDescription = null,tint = cor)
            }

            IconButton({
                scop.launch(Dispatchers.Main) {
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
                Log.e("setmod","entrou no botao de repeticao")
                scop.launch(Dispatchers.Main) {
                    when(modoRepeticao.value){
                        is ModoDerepeticao.Desativado->vm.setModoDeRepeticao(ModoDerepeticao.RepetirEssa)
                        is ModoDerepeticao.RepetirEssa->vm.setModoDeRepeticao(ModoDerepeticao.RepetirTodos)
                        is ModoDerepeticao.RepetirTodos->vm.setModoDeRepeticao(ModoDerepeticao.Desativado)
                    }
                }


            }) {
                when(modoRepeticao.value){
                    is ModoDerepeticao.Desativado-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint =cor)
                    is ModoDerepeticao.RepetirEssa-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24), contentDescription = null,tint = cor)
                    is ModoDerepeticao.RepetirTodos-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24), contentDescription = null,tint = cor)
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


@Composable
fun BotoesDeControle(vm: VmodelPlayer,
                     scop: CoroutineScope,
                     columnScope: ColumnScope,
                     modoAleatorio: State<Boolean>,
                     reproduzindo: State<Boolean>,
                     modoRepeticao: State<ModoDerepeticao>, cor: Color
){
    with(columnScope) {
        Row (modifier = Modifier.fillMaxWidth().fillMaxHeight(0.40f),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
            IconButton({scop.launch { vm.preview() }}) {
                Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = cor)
            }
            IconButton({
                scop.launch(Dispatchers.Main) {
                    vm.setModoAleatorio(!modoAleatorio.value)}
            }) {
                if(!modoAleatorio.value)
                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = cor)
                else
                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_on_24), contentDescription = null,tint = cor)
            }

            IconButton({
                scop.launch(Dispatchers.Main) {
                    if(reproduzindo.value)
                        vm.pause()
                    else vm.play()
                }

            }) {
                if (reproduzindo.value)
                    Icon(painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = null,
                        tint = cor,
                        modifier = Modifier)
                else
                    Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = null,
                        tint = cor,
                        modifier = Modifier)
            }

            IconButton({
                scop.launch(Dispatchers.Main) {
                    when(modoRepeticao.value){
                        is ModoDerepeticao.Desativado->vm.setModoDeRepeticao(ModoDerepeticao.RepetirEssa)
                        is ModoDerepeticao.RepetirEssa->vm.setModoDeRepeticao(ModoDerepeticao.RepetirTodos)
                        is ModoDerepeticao.RepetirTodos->vm.setModoDeRepeticao(ModoDerepeticao.Desativado)
                    }
                }


            }) {
                when(modoRepeticao.value){
                    is ModoDerepeticao.Desativado-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint =cor)
                    is ModoDerepeticao.RepetirEssa-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_one_on_24), contentDescription = null,tint = cor)
                    is ModoDerepeticao.RepetirTodos-> Icon(painter = painterResource(id = R.drawable.baseline_repeat_on_24), contentDescription = null,tint = cor)
                }

            }
            IconButton({vm.next()}) {
                Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                    contentDescription = null,
                    tint = cor,modifier = Modifier)
            }

        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ImagemPlyer(imagem: State<ImagemPlyer>,
                iconsize:Float,
                sharedTransitionScope: SharedTransitionScope,
                animatedVisibilityScope: AnimatedVisibilityScope,
                cor: Color
){
    with(sharedTransitionScope){
        when(val r=imagem.value){
            is ImagemPlyer.Vazia-> Icon(painter = painterResource(id =r.icone),
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
                    contentScale = ContentScale.FillBounds)
            }
        }}
}
@Composable
fun ImagemPlyer(imagem: State<ImagemPlyer>,
                iconeSize: Float,
                corDotexto:Color,
                scope: ColumnScope){
    with(scope){
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
    }
}

@Composable
fun ListaPlyLIsts(vm: VmodelPlayer,
                  vmlista: ViewModelListas,
                  backgraudColor: Color,
                  acaoNavegarOpcoesItemsDaListaAberta: (MediaItem?) -> Unit){
    val  metadata=vm._mediaItemAtual.collectAsState()
    val indice=vm._indice.collectAsState()
    val listState= rememberLazyListState(initialFirstVisibleItemIndex = indice.value)
    val estadoPlylist=vmlista._estadoPlylsist.collectAsState()
    val lista =vmlista.plylist().collectAsState(emptyList())
    LaunchedEffect(estadoPlylist.value) {
        Log.e("estado da playlist",estadoPlylist.value.toString())
    }
    LazyColumn(state =  listState,modifier=Modifier.background(color = backgraudColor).fillMaxWidth().fillMaxHeight()) {

        itemsIndexed(items = lista.value) {indice,item->
            if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, )
                    ItemDaLista(Modifier.clickable {
                        vm.seekToItem(indice)

                    },item = item,  vm = vmlista, acaoNavegarOpcoes = acaoNavegarOpcoesItemsDaListaAberta)
                }
            else
                ItemDaLista(Modifier.clickable {
                    vm.seekToItem(indice)

                },item = item, vm = vmlista,acaoNavegarOpcoes = acaoNavegarOpcoesItemsDaListaAberta)
        }
    }
}

@Composable
fun ListaPlyLIsts(vm: VmodelPlayer,
                  vmlista: ViewModelListas,
                  backgraudColor: Color,
                  corDotexto: Color,
                  acaoNavegarOpcoesItemsDaListaAberta: (MediaItem?) -> Unit){
    val  metadata=vm._mediaItemAtual.collectAsState()
    val indice=vm._indice.collectAsState()
    val listState= rememberLazyListState(initialFirstVisibleItemIndex = indice.value)
    val estadoPlylist=vmlista._estadoPlylsist.collectAsState()
    val lista =vmlista.plylist().collectAsState(emptyList())

    LazyColumn(state =  listState,modifier=Modifier.background(color = backgraudColor).fillMaxWidth().fillMaxHeight()) {

        itemsIndexed(items = lista.value) {indice,item->
            if(metadata.value!=null&& item.mediaId==metadata.value!!.mediaId)
                Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Icon(painter = painterResource(R.drawable.baseline_play_arrow_24,), contentDescription = null, tint = corDotexto )
                    ItemDaLista(Modifier.clickable {
                        vm.seekToItem(indice)

                    },item = item, cor = corDotexto, vm = vmlista,acaoNavegarOpcoes = acaoNavegarOpcoesItemsDaListaAberta)
                }
            else
                ItemDaLista(Modifier.clickable {
                    vm.seekToItem(indice)

                },item = item,cor = corDotexto, vm = vmlista,acaoNavegarOpcoes = acaoNavegarOpcoesItemsDaListaAberta)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ApresenttacaoDasPlyListsPlyerCompat(sharedTransitionScope: SharedTransitionScope,
                                        animatedVisibilityScope: AnimatedVisibilityScope,scope: CoroutineScope,
                                        backgraudColor:Color,
                                        corDoTexto: MutableState<Color>,
                                        cor: MutableState<Color>,
                                        textColorSquemas:Color,
                                        vm: VmodelPlayer,
                                        vmlista: ViewModelListas,
                                        listaAvberta: MutableState<Boolean>,
                                        acaoNavegarOpcoesItemsDaListaAberta:(MediaItem?)->Unit,
){
    with(sharedTransitionScope) {
        Column(
            Modifier.sharedBounds(
                rememberSharedContentState(key = LayoutsCompartilhados.LayoutPluer.label),
                animatedVisibilityScope,
                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
            )
                .background(backgraudColor)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                MiniplayerParaTransicao(
                    sharedTransitionScope =this@with,
                    animatedVisibilityScope = animatedVisibilityScope,
                    vm = vm,
                    vmListas = vmlista,
                    corDotexto = corDoTexto.value,
                    backgraud = cor.value,
                    acaoMudarBackgraud = {
                        AuxiliarMudancaDeBackGrands().mudarBackgrandMiniPlyer(
                            it,
                            backgraudColor = cor,
                            corTexto = corDoTexto,
                            textColorSquemas = textColorSquemas,
                            backgraudColorSquemas = backgraudColor
                        )
                    }
                )
            }
            Spacer(Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) { Banner()
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                IconButton(onClick = {
                    listaAvberta.value = !listaAvberta.value
                }, modifier = Modifier.size(60.dp)) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = DarkPink,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
            ListaPlyLIsts(vm, vmlista, backgraudColor,acaoNavegarOpcoesItemsDaListaAberta)

        }
    }
}

@Composable
fun ApresentacaoPlyListPlyerEstendido(rowScope: RowScope,
                                      vmodelPlayer: VmodelPlayer,
                                      vmlista: ViewModelListas,
                                      backgraudColor:Color,
                                      corDotexto: Color,
                                      acaoNavegarOpcoesItemsDaListaAberta: (MediaItem?) -> Unit){
    with(rowScope){
    Column(
        Modifier.clip(RoundedCornerShape(15.dp)).background(backgraudColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Playlist", fontFamily = FontFamily.Monospace, color = corDotexto)
        Spacer(Modifier.padding(3.dp))
        Banner()
        ListaPlyLIsts(vmodelPlayer, vmlista, backgraudColor,corDotexto,acaoNavegarOpcoesItemsDaListaAberta)

    }}
}
