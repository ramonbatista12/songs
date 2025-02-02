package com.example.songs.componentes.dialog

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.songs.componentes.ItemsListaPlaylistsLista
import com.example.songs.navegacao.DestinosDENavegacao
import com.example.songs.viewModels.ViewModelListas

@Composable
fun DialogoOpcoesItemsDaLista(modifier: Modifier =Modifier,
                              text:String="Opcoes",
                              acaoDeCompartilhar:(uri:Uri)->Unit={},
                              acaoAdicionarPlaylist:(nome:String)->Unit={},acaoDeCancelar:()->Unit={}){
    Dialog(onDismissRequest = { acaoDeCancelar() }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {

            Box(modifier = Modifier.fillMaxSize()){
                  Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                  Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp)) {
                     ItemsDialogoOpcoesItems(item = ObjetosDeDialog.Compartilha,
                                             modifier = Modifier.clickable { acaoDeCompartilhar(Uri.parse("https://www.google.com")) })
                     Spacer(modifier = Modifier.padding(10.dp))
                     ItemsDialogoOpcoesItems(item = ObjetosDeDialog.AdicionarAplyList,
                                             modifier = Modifier.clickable { acaoAdicionarPlaylist("Nova Playlist") })
                  }

            }
        }
    }
}

@Composable
fun DialogoOpcoesItemsAlbums(modifier: Modifier =Modifier,
                              text:String="Opcoes",
                              acaoDeCompartilhar:(uri:Uri)->Unit={},
                              acaoAdicionarPlaylist:(nome:String)->Unit={},acaoDeCancelar:()->Unit={},
                              idDoAlbums:Long,
                              vm: ViewModelListas){
    Dialog(onDismissRequest = { acaoDeCancelar() }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {

            Box(modifier = Modifier.fillMaxSize()){
                Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp)) {


                    ItemsDialogoOpcoesItems(item = ObjetosDeDialog.AdicionarAplyList,
                        modifier = Modifier.clickable {  })
                }

            }
        }
    }
}


@Composable
fun DialogoCriarPlyList(modifier: Modifier =Modifier,
                   text:String="Nova Playlist",
                   acaoCamcelar:()->Unit={},
                   acaoAdicionarPlaylist:(nome:String)->Unit={},
                   vm:ViewModelListas){
    Dialog(onDismissRequest = { }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {
        val textoDigitado= rememberSaveable { mutableStateOf("") }
            Box(modifier = Modifier.fillMaxSize()){
                Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp),
                       horizontalAlignment = Alignment.CenterHorizontally) {
                  OutlinedTextField(value = textoDigitado.value,
                                    onValueChange = {textoDigitado.value=it},
                                    modifier = Modifier.width(250.dp),
                                    label = { Text(text = "Titulo")})

            }
            Row(modifier=Modifier.align(Alignment.BottomCenter))  {
                 TextButton(onClick = {acaoCamcelar()}) {
                     Text(text = "Cancelar")
                 }
                 TextButton(onClick = {
                     vm.criarNovaPlylist(textoDigitado.value,
                                         acaoDecomclusao = {acaoCamcelar()})
                 }) {
                     Text(text = "Criar")
                 }
            }
        }
    }



}}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoDeSelecaoDePlyList(item:DestinosDENavegacao.DestinosDeDialogo.AdiconarPlaylist,acaoCamcelar: () -> Unit={},vm: ViewModelListas){
 val sheetState =rememberModalBottomSheetState()
 LaunchedEffect(Unit) {
     sheetState.show()
 }
 val plylist =vm.plylist.collectAsState()


ModalBottomSheet (onDismissRequest = acaoCamcelar,sheetState = sheetState)  {
     OutlinedCard {
         Column(horizontalAlignment = Alignment.CenterHorizontally){
             LazyColumn{
                 items(items = plylist.value){
                     ItemsListaPlaylistsLista(modifier = Modifier.clickable {
                         vm.adicionarMusicaNaPlyList(MediaItem.Builder().setMediaId(item.id)
                                                                        .setUri(item.uri)
                                                                        .setMediaMetadata(MediaMetadata.Builder().setAlbumArtist(item.album)
                                                                                                                 .setTitle(item.titulo)
                                                                                                                 .setDurationMs(item.duracao.toLong())
                                                                                                                 .setArtist(item.artista)
                                                                                                                 .setArtworkUri(Uri.parse(item.uri))
                                                                                                                 .build()).build(),
                                                       idPlylist = it.id, acaoDecomclusao = acaoCamcelar)
                     },item = it)
                 }
             }
         }

     }

 }


}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviaDialog(){
 MaterialTheme {


 Surface {
 Scaffold(Modifier.fillMaxSize()) {
       DialogoOpcoesItemsDaLista()
 }
 }
 }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviaDialogocriarPlylist(){
 MaterialTheme {


     Surface {
         Scaffold(Modifier.fillMaxSize()) {
          //   DialogoCriarPlyList()
         }
     }
 }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(showBackground = true)
fun PreviaDialogDeSelecaoDePlyList(){
 MaterialTheme {
     Surface {
         Scaffold(Modifier.fillMaxSize()) {
            // DialogoDeSelecaoDePlyList()
         }
     }
 }
}