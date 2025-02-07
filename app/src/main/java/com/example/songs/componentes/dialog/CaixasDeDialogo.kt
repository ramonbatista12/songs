package com.example.songs.componentes.dialog

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import com.example.songs.componentes.ItemsListaPlaylists
import com.example.songs.navegacao.DestinosDENavegacao
import com.example.songs.viewModels.ViewModelListas

@Composable
fun DialogoOpcoesItemsDaLista(modifier: Modifier =Modifier,
                              text:String="Opcoes",
                              acaoDeCompartilhar:(uri:Uri)->Unit={},
                              acaoAdicionarPlaylist:(nome:String)->Unit={},
                              acaoDeCancelar:()->Unit={},
                              acaoRemoverDaPlylist:()->Unit={} ,
                              estaNaplylist:Boolean){
    Dialog(onDismissRequest = { acaoDeCancelar() }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {

            Box(modifier = Modifier.fillMaxSize()){
                  Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                  Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp)) {
                     ItemsDialogoOpcoesItems(item = ObjetosDeDialogOpcoesItemsListaMusicas.Compartilha,
                                             modifier = Modifier.clickable { acaoDeCompartilhar(Uri.parse("https://www.google.com")) })
                     Spacer(modifier = Modifier.padding(10.dp))
                     ItemsDialogoOpcoesItems(item = ObjetosDeDialogOpcoesItemsListaMusicas.AdicionarAplyList,
                                             modifier = Modifier.clickable { acaoAdicionarPlaylist("Nova Playlist") })
                      Spacer(modifier=Modifier.padding(10.dp))
                      if(estaNaplylist)
                      ItemsDialogoOpcoesItems(modifier = Modifier.clickable { acaoRemoverDaPlylist() }, item = ObjetosDeDialogOpcoesItemsListaMusicas.AdicionarRemoverDaPlylist)
                  }

            }
        }
    }
}




@Composable
fun DialogoOpcoesPlalystOpcoes(modifier: Modifier =Modifier,
                              text:String="Opcoes",
                              acaoDeApagarPlylist:()->Unit={},
                              acaoRenomearPlylis:()->Unit={},
                              acaoDeCancelar:()->Unit={}){
    Dialog(onDismissRequest = { acaoDeCancelar() }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {

            Box(modifier = Modifier.fillMaxSize()){
                Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp)) {
                   ItemsDialogoOpcoesPlyList(modifier = Modifier.clickable {acaoRenomearPlylis()},
                                             item = ObjetosDeDialogOpcoesPlyList.Editar)
                    Spacer(modifier = Modifier.padding(10.dp))
                   ItemsDialogoOpcoesPlyList(modifier = Modifier.clickable { acaoDeApagarPlylist()  },
                                             item = ObjetosDeDialogOpcoesPlyList.Apagar)
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


                    ItemsDialogoOpcoesItems(item = ObjetosDeDialogOpcoesItemsListaMusicas.AdicionarAplyList,
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

@Composable
fun DialogoEditarPlyList(modifier: Modifier =Modifier,
                        text:String="Novo Titulo",
                        acaoCamcelar:()->Unit={},
                        acaoAdicionarPlaylist:(nome:String)->Unit={},
                        vm:ViewModelListas,
                        plyListEditacao:DestinosDENavegacao.DestinosDeDialogo.EditarPlyList ){
    Dialog(onDismissRequest = { }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {
            val textoDigitado= rememberSaveable { mutableStateOf(plyListEditacao.titulo) }
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
                    TextButton(onClick = { acaoCamcelar()}) {
                        Text(text = "Cancelar")
                    }
                    TextButton(onClick = {
                        vm.editarTituloPlyList(id = plyListEditacao.id,textoDigitado.value, acaoDecomclusao = acaoCamcelar)
                    }) {
                        Text(text = "Criar")
                    }
                }
            }
        }



    }}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoDeSelecaoDePlyList(item:DestinosDENavegacao.DestinosDeDialogo.AdiconarPlaylist,
                              acaoCamcelar: () -> Unit={},
                              acaoCriarNovaLista:()->Unit={},
                              vm: ViewModelListas){
 val sheetState =rememberModalBottomSheetState()
 LaunchedEffect(Unit) {
     sheetState.show()
 }
 val plylist =vm.plylist.collectAsState()


ModalBottomSheet (onDismissRequest = acaoCamcelar,sheetState = sheetState)  {
     OutlinedCard {
         Column(horizontalAlignment = Alignment.CenterHorizontally){
             LazyColumn{
                 item {
                     Spacer(modifier = Modifier.padding(20.dp))
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                      OutlinedButton(onClick = acaoCriarNovaLista)   { Text("Criar nova")
                           Icon(Icons.Rounded.AddCircle,contentDescription = null)
                     }}
                 Spacer(Modifier.padding(4.dp))
                 }
                 items(items = plylist.value){
                     ItemsListaPlaylists(modifier = Modifier.clickable {
                         vm.adicionarMusicaNaPlyList(MediaItem.Builder().setMediaId(item.id)
                                                                        .setUri(item.uri)
                                                                        .setMediaMetadata(MediaMetadata.Builder().setAlbumArtist(item.album)
                                                                                                                 .setTitle(item.titulo)
                                                                                                                 .setDurationMs(item.duracao.toLong())
                                                                                                                 .setArtist(item.artista)
                                                                                                                 .setArtworkUri(Uri.parse(item.uri))
                                                                                                                 .build()).build(),
                                                       idPlylist = it.id, acaoDecomclusao = acaoCamcelar)
                     }, vm = vm,item = it)
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
     //  DialogoOpcoesItemsDaLista()
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