package com.example.songs.componentes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.songs.R
import com.example.songs.navegacao.DestinosDENavegacao

/*
* aqui esta presente os componentes para o scafolld
* como topbar ou bottonbar
* */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperio(modifier: Modifier=Modifier,titulo: String){
    TopAppBar(title = { Text(text = titulo) },modifier = modifier, navigationIcon = { Icon(painter = painterResource(id = R.drawable.baseline_music_note_24), contentDescription = null) })

}



@Composable
fun BararInferior(modifier: Modifier=Modifier,acaoNavegacao:(ob:DestinosDENavegacao)->Unit){
    NavigationBar(modifier=modifier.fillMaxWidth().background(Color.Transparent),containerColor = MaterialTheme.colorScheme.background,contentColor = MaterialTheme.colorScheme.onBackground) {
        Icones.list.forEach {
            NavigationBarItem(selected = false, onClick = {
                 acaoNavegacao(it.rota)
            },
                              icon = { Icon(painter = painterResource(id = it.icone),
                                            contentDescription = null) }
                               )
        }

    }

}


@Composable
fun PermanenteNavigationDrawer(modifier: Modifier=Modifier,acaoNavegacao: (ob:DestinosDENavegacao) -> Unit){


    PermanentDrawerSheet(modifier=modifier.width(90.dp)) {
        Column {
         Icones.list.forEach {
             NavigationDrawerItem(label ={ Text("") },
                 icon = { Icon(painter = painterResource(it.icone), contentDescription = null) },
                 onClick = {
                      acaoNavegacao(it.rota)
                 }, modifier = Modifier, selected = false)

         }


    }


}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviaTopBar(){

    Scaffold(topBar = { BarraSuperio(titulo = "teste") }) {

    }



}