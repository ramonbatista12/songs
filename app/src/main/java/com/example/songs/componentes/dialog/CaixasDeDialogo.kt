package com.example.songs.componentes.dialog

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.w3c.dom.Text

@Composable
fun DialogoOpcoesItemsDaLista(modifier: Modifier =Modifier,
                              text:String="Opcoes",
                              acaoDeCompartilhar:(uri:Uri)->Unit={},
                              acaoAdicionarPlaylist:(nome:String)->Unit={}){
    Dialog(onDismissRequest = { }) {
        //aqui vai o conteudo do dialogo
        OutlinedCard(modifier = modifier.width(280.dp).height(200.dp)) {

            Box(modifier = Modifier.fillMaxSize()){
                  Text(text = text,modifier=Modifier.align(Alignment.TopCenter))
                  Column(modifier=Modifier.align(Alignment.CenterStart).padding(start = 20.dp)) {
                      ObjetosDeDialog.lista.forEach {
                          DialogoOpcoesItemsDaLista(item = it)
                          Spacer(modifier=Modifier.padding(10.dp))
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