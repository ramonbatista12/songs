package com.example.songs.navegacao

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.ItemDaLista
import com.example.songs.componentes.ItemsListaColunas
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.songs.application.AplicationCuston
import com.example.songs.componentes.ItemsAlbums
import com.example.songs.componentes.ItemsAlbusColuna
import com.example.songs.componentes.dialog.DialogoCriarPlyList
import com.example.songs.componentes.dialog.DialogoDeSelecaoDePlyList
import com.example.songs.componentes.dialog.DialogoEditarPlyList
import com.example.songs.componentes.dialog.DialogoOpcoesItemsAlbums
import com.example.songs.componentes.dialog.DialogoOpcoesItemsDaLista
import com.example.songs.componentes.dialog.DialogoOpcoesPlalystOpcoes
import com.example.songs.componentes.paineis.AlbumId
import com.example.songs.componentes.paineis.ArtistaId
import com.example.songs.componentes.paineis.BigPlayer
import com.example.songs.componentes.paineis.ListaDeAlbums
import com.example.songs.componentes.paineis.ListaDeArtistas
import com.example.songs.componentes.paineis.ListaDemusicas
import com.example.songs.componentes.paineis.PlayListId
import com.example.songs.componentes.paineis.PlyList
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.viewModels.FabricaViewModelLista
import com.example.songs.viewModels.FabricaViewModelPlylist
import com.example.songs.viewModels.ViewModelListas
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.runner.manipulation.Ordering
import java.io.File

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
              estadoService:MutableStateFlow<ResultadosConecaoServiceMedia>){
    val vmLista:ViewModelListas=viewModel(factory = FabricaViewModelLista().fabricar(r= AplicationCuston.conteiner.repositorio,estadoService))
    val scope=rememberCoroutineScope()
NavHost(navController = navController, startDestination = DestinosDENavegacao.DestinosDeTela.Todas,modifier=modifier){
  composable<DestinosDENavegacao.DestinosDeTela.Todas>{
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
            acaoNavegarOpcoes = {scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeDialogo.OpcoesPlaylist(id=it ?:0)) }})
       }}


  composable<DestinosDENavegacao.DestinosDeTela.Album>{
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
                vm = vm,acaoAvisoBigplyer = acaoAvisoBigplyer,
          vmlista =viewModel(factory = FabricaViewModelLista().fabricar(AplicationCuston.repositorio,estadoService)), acaoDeVoutar = {navController.popBackStack()}
           , acaMudarBackgraudScafolld = {acaoMudaBackgraundScafolld(it)},
          acaoMudarCorScafollEBArraPermanente = acaoMudarcorBackgrandEBarraPermanent)

  }

  composable<DestinosDENavegacao.DestinosDeTela.Artista>{
      ListaDeArtistas(windowSizeClass = windowSizeClass,
                      transicaoMiniPlyer = transicaoMiniPlyer,
                      acaoNavegarPorId ={s->
                          Log.d("id artista", "Navgrafic: $s")
                      scope.launch { navController.navigate(DestinosDENavegacao.DestinosDeTela.ArtistaId(s.toLong()))
                                     }},
                      vmodel = vmLista)

  }

  composable<DestinosDENavegacao.DestinosDeTela.ArtistaId>{

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