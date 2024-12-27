package com.example.songs.componentes

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.songs.R
/*
* aqui esta presente os componentes para o scafolld
* como topbar ou bottonbar
* */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperio(modifier: Modifier=Modifier,titulo: String){
    TopAppBar(title = { Text(text = titulo) },modifier = modifier, navigationIcon = { Icon(painter = painterResource(id = R.drawable.baseline_music_note_24), contentDescription = null) })

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviaTopBar(){

    Scaffold(topBar = { BarraSuperio(titulo = "teste") }) {

    }



}