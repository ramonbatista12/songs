package com.songsSongs.songs.navegacao

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.songsSongs.songs.application.AplicationCuston
import com.songsSongs.songs.componentes.dialog.DialogoCriarPlyList
import com.songsSongs.songs.componentes.dialog.DialogoDeSelecaoDePlyList
import com.songsSongs.songs.componentes.dialog.DialogoEditarPlyList
import com.songsSongs.songs.componentes.dialog.DialogoOpcoesItemsAlbums
import com.songsSongs.songs.componentes.dialog.DialogoOpcoesItemsDaLista
import com.songsSongs.songs.componentes.dialog.DialogoOpcoesPlalystOpcoes
import com.songsSongs.songs.componentes.paineis.AlbumId
import com.songsSongs.songs.componentes.paineis.ArtistaId
import com.songsSongs.songs.componentes.paineis.BigPlayer
import com.songsSongs.songs.componentes.paineis.ListaDeAlbums
import com.songsSongs.songs.componentes.paineis.ListaDeArtistas
import com.songsSongs.songs.componentes.paineis.ListaDemusicas
import com.songsSongs.songs.componentes.paineis.PlayListId
import com.songsSongs.songs.componentes.paineis.PlyList
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.songsSongs.songs.viewModels.FabricaViewModelLista
import com.songsSongs.songs.viewModels.ViewModelListas
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
* Navgrafic e o grafico de navegacao em sim suas rotas sao determinadas pela classe DestinosDENavegacao
* rotas disponiveis  :Todas,Playlist,Album,Configuracoes,Player
* cada funcoes e responsavel por adiministra suas funcoes como reproduzir ou listar as musicas
* mas cada funcao  e responsavel por checar a o tamanho da classe de janela
* e determinar camo deve seer esibido seus dados em alguns casos
* e determinado um painel separado para diferentes tamanhos de tela
*
* */


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navgrafic(navController: NavHostController,
              windowSizeClass: WindowSizeClass,
              modifier: Modifier,
              paddingValues: PaddingValues,
              transicaoMiniPlyer:MutableTransitionState<Boolean>,
              vm: VmodelPlayer,
              acaoCaregarPlyer:(List<MediaItem>,indice:Int)->Unit,
              acaoAvisoBigplyer:()->Unit,acaoMudaBackgraundScafolld:(Color)->Unit={},
              acaoMudarcorBackgrandEBarraPermanent:(backgrand:Color,corBarra:Color)->Unit={b,c->},
               acaOcultarBaras:()->Unit={},
              acaOnMostraBaras:()->Unit={},
              estadoService:MutableStateFlow<ResultadosConecaoServiceMedia>){
    val vmLista:ViewModelListas=viewModel(factory = FabricaViewModelLista().fabricar(r= AplicationCuston.conteiner.repositorio,estadoService))
    val scope=rememberCoroutineScope()
    val context= LocalActivity.current
    val altura= LocalConfiguration.current.screenHeightDp

NavHost(navController = navController,
        startDestination = DestinosDENavegacao.DestinosDeTela.Todas,
        modifier=modifier,
        exitTransition = {//EaseOutBack
            fadeOut(animationSpec = snap()  )/*+ slideOutVertically(animationSpec = tween(durationMillis = 100, delayMillis = 200, easing = EaseOutCubic))*/
         },
         enterTransition = {
              fadeIn(animationSpec = snap()) }){
  composable<DestinosDENavegacao.DestinosDeTela.Todas>{
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
   Box{
       ListaDemusicas(modifier = Modifier,
                      windowSizeClass = windowSizeClass,
                      paddingValues = paddingValues,
                      transicaoMiniPlyer = transicaoMiniPlyer,
                      viewModelListas = vmLista,
                      acaoCarregarPlyer = acaoCaregarPlyer,acaoNavegarOpcoes = {

                              val metadata=it?.mediaMetadata
               navController.navigate(   DestinosDENavegacao.DestinosDeDialogo
                                                             .OpcoesItemsDaLista(titulo = metadata?.title.toString(),
                                                                                 artista = metadata?.artist.toString(),
                                                                                 uri =metadata?.artworkUri.toString(),
                                                                                 album = metadata?.albumArtist.toString(),
                                                                                 id=it?.mediaId.toString(),
                                                                                 duracao = metadata?.durationMs.toString() ))

           })
   }
  }

  composable<DestinosDENavegacao.DestinosDeTela.Playlist>{
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
      Box{
        PlyList(modifier = modifier,
                windowSizeClass = windowSizeClass,
                paddingValues = paddingValues,
                transicaoMiniPlyer = transicaoMiniPlyer,
                vm = vmLista,
                acaoNavegarDialoCriarPlaylist = {
                    scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeDialogo.CriarPlaylist)} },
            acaONavegacao = {id->
                scope.launch {
                    navController.navigate(DestinosDENavegacao.DestinosDeTela.PlyListId(id=id))
                }
            },
            acaoNavegarOpcoes = {scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeDialogo.OpcoesPlaylist(id=it ?:0)) }},
            acaoNavegarIdAlbum = {s->
                Log.d("id artista", "Navgrafic: $s")
                scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.AlbumId(s.toLong()))}
            },
            acaoNavegarIdArtista = {s->
                Log.d("id artista", "Navgrafic: $s")
                scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.ArtistaId(s.toLong()))
                }
            })
       }}


  composable<DestinosDENavegacao.DestinosDeTela.Album>{
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
      Box(modifier=Modifier){
          ListaDeAlbums(windowSizeClass = windowSizeClass,
                       transicaoMiniPlyer = transicaoMiniPlyer,
                       vm =vmLista,
                       acaoNavegarPorId = {s->
                           Log.d("id artista", "Navgrafic: $s")
                       scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.AlbumId(s.toLong()))}})
         }}


  composable<DestinosDENavegacao.DestinosDeTela.Configuracoes>{}

  //
  composable<DestinosDENavegacao.DestinosDeTela.Player>{



      BigPlayer(windowSizeClass = windowSizeClass,
          modifier = Modifier,
          paddingValues = paddingValues,
          vm = vm,
          acaoAvisoBigplyer = acaoAvisoBigplyer,
          vmlista = viewModel(factory = FabricaViewModelLista().fabricar(AplicationCuston.repositorio,estadoService)), acaoDeVoutar = {navController.popBackStack()},
          acaMudarBackgraudScafolld = {acaoMudaBackgraundScafolld(it)},
          acaoMudarCorScafollEBArraPermanente = acaoMudarcorBackgrandEBarraPermanent,
          acaoOcultarBaras = acaOcultarBaras,
          acaoMostraBaras = acaOnMostraBaras)

  }

  composable<DestinosDENavegacao.DestinosDeTela.Artista>{
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
      ListaDeArtistas(windowSizeClass = windowSizeClass,
                      transicaoMiniPlyer = transicaoMiniPlyer,
                      acaoNavegarPorId ={s->
                          Log.d("id artista", "Navgrafic: $s")
                      scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.ArtistaId(s.toLong()))
                                     }},
                      vmodel = vmLista)

  }

  composable<DestinosDENavegacao.DestinosDeTela.ArtistaId>{
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
      val artistaId:DestinosDENavegacao.DestinosDeTela.ArtistaId=it.toRoute()
      ArtistaId(modifier = Modifier,
          windowSizeClass = windowSizeClass,
          paddingValues = paddingValues,
          transicaoMiniPlyer = transicaoMiniPlyer,
          viewModelListas = vmLista,
          acaoCarregarPlyer = acaoCaregarPlyer,
          id=artistaId.id, acaoNavegarOpcoes = {
            scope.launch {
              val metadata=it?.mediaMetadata
              navController.navigate(   DestinosDENavegacao.DestinosDeDialogo
                  .OpcoesItemsDaLista(titulo = metadata?.title.toString(),
                      uri =metadata?.artworkUri.toString(),
                      artista = metadata?.artist.toString(),
                      album = metadata?.albumArtist.toString(),
                      id=it?.mediaId.toString(),
                      duracao = metadata?.durationMs.toString() ))}
          })
  }

  composable<DestinosDENavegacao.DestinosDeTela.PlyListId> {

      val id =it.toRoute<DestinosDENavegacao.DestinosDeTela.PlyListId>()
      val backgrand = MaterialTheme.colorScheme.background
      val corBarra= MaterialTheme.colorScheme.onBackground
      LaunchedEffect(Unit) {
          acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
          acaoMudaBackgraundScafolld(backgrand)
      }
      PlayListId(modifier = Modifier,
          windowSizeClass = windowSizeClass,
          paddingValues = paddingValues,
          transicaoMiniPlyer = transicaoMiniPlyer,
          vm = vmLista,
          acaoCarregarPlyer = acaoCaregarPlyer,
          id=id.id,
          acaoNavegarOpcoes = {

              val metadata=it?.mediaMetadata
              navController.navigate(   DestinosDENavegacao.DestinosDeDialogo
                  .OpcoesItemsDaLista(titulo = metadata?.title.toString(),
                      artista = metadata?.artist.toString(),
                      uri =metadata?.artworkUri.toString(),
                      album = metadata?.albumArtist.toString(),
                      id=it?.mediaId.toString(),
                      duracao = metadata?.durationMs.toString(), estraNaplylist = true ))

          })
  }

   composable<DestinosDENavegacao.DestinosDeTela.AlbumId>{
       val id:DestinosDENavegacao.DestinosDeTela.AlbumId=it.toRoute()
       val backgrand = MaterialTheme.colorScheme.background
       val corBarra= MaterialTheme.colorScheme.onBackground
       LaunchedEffect(Unit) {
           acaoMudarcorBackgrandEBarraPermanent(backgrand,corBarra)
           acaoMudaBackgraundScafolld(backgrand)
       }
       AlbumId(
           modifier = Modifier,
           windowSizeClass = windowSizeClass,
           paddingValues = paddingValues,
           transicaoMiniPlyer = transicaoMiniPlyer,
           viewModelListas = vmLista,
           acaoCarregarPlyer = acaoCaregarPlyer,
           id=id.id,
           acaoNavegarOpcoes = {
             scope.launch {
               val metadata=it?.mediaMetadata
               navController.navigate(   DestinosDENavegacao.DestinosDeDialogo
                   .OpcoesItemsDaLista(titulo = metadata?.title.toString(),
                       uri =metadata?.artworkUri.toString(),
                       artista = metadata?.artist.toString(),
                       album = metadata?.albumArtist.toString(),
                       id=it?.mediaId.toString(),
                       duracao = metadata?.durationMs.toString() ))}
           }
       )

   }
   dialog<DestinosDENavegacao.DestinosDeDialogo.OpcoesItemsDaLista> {
       val objeto= it.toRoute<DestinosDENavegacao.DestinosDeDialogo.OpcoesItemsDaLista>()
       val comtext= LocalContext.current
       DialogoOpcoesItemsDaLista(acaoDeCompartilhar = {
          val intent= Intent().apply {
              action= Intent.ACTION_SEND
              putExtra(Intent.EXTRA_STREAM,Uri.parse(objeto.uri))
              type="audio/*"
          }
         comtext.startActivity(Intent.createChooser(intent,null))

       },
                                 acaoAdicionarPlaylist = {
                                     scope.launch {
                                      navController.navigate(DestinosDENavegacao.DestinosDeDialogo
                                                                               .AdiconarPlaylist(titulo = objeto.titulo,
                                                                                                artista = objeto.artista ,
                                                                                                 uri = objeto.uri,
                                                                                                 album = objeto.album,
                                                                                                 id = objeto.id,
                                                                                                 duracao = objeto.duracao))}
                                 },
                                 acaoDeCancelar = {navController.popBackStack()},
                                 acaoRemoverDaPlylist = {
                                     vmLista.removerDaPlyList(idMedia = objeto.id,{navController.popBackStack()})
                                 }, estaNaplylist = objeto.estraNaplylist)

   }

   dialog<DestinosDENavegacao.DestinosDeDialogo.OpcoesPlaylist>{
       val objeto= it.toRoute<DestinosDENavegacao.DestinosDeDialogo.OpcoesPlaylist>()
       DialogoOpcoesPlalystOpcoes(acaoDeApagarPlylist = { vmLista.excluirPlyList(objeto.id,
                                                                                 acaoDecomclusao = { navController.popBackStack()})},
                                  acaoDeCancelar = {
                                      scope.launch { navController.popBackStack() }},
                                  acaoRenomearPlylis = {
                                      scope.launch {
                                          navController.navigate(DestinosDENavegacao.DestinosDeDialogo.EditarPlyList(id=objeto.id, titulo = ""))
                                      }
                                  })
   }

   dialog<DestinosDENavegacao.DestinosDeDialogo.AdiconarPlaylist> {
       val objeto= it.toRoute<DestinosDENavegacao.DestinosDeDialogo.AdiconarPlaylist>()
      DialogoDeSelecaoDePlyList(item = objeto,
                                acaoCamcelar = {scope.launch {navController.popBackStack()}},
                                acaoCriarNovaLista = {scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeDialogo.CriarPlaylist) }},
                                vm = vmLista)
   }

   dialog<DestinosDENavegacao.DestinosDeDialogo.OpcoesItemsAlbums>{
       val albumid=it.toRoute<DestinosDENavegacao.DestinosDeDialogo.OpcoesItemsAlbums>()
       DialogoOpcoesItemsAlbums(acaoDeCompartilhar = {},
                                acaoDeCancelar = {scope.launch {navController.popBackStack()  }}, idDoAlbums = albumid.idDoAlbum,vm=vmLista)
   }

   dialog<DestinosDENavegacao.DestinosDeDialogo.CriarPlaylist> {



       DialogoCriarPlyList(acaoAdicionarPlaylist = {},
                           acaoCamcelar = { scope.launch { navController.popBackStack() }},
                           vm = vmLista )
   }

   dialog<DestinosDENavegacao.DestinosDeDialogo.EditarPlyList> {
       val plylist= it.toRoute<DestinosDENavegacao.DestinosDeDialogo.EditarPlyList>()
       DialogoEditarPlyList(vm=vmLista, acaoCamcelar = {navController.popBackStack()},plyListEditacao = plylist )
   }

}


}