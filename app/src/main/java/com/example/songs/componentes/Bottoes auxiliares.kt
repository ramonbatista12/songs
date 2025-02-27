package com.example.songs.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.songs.R

@Composable
fun BotaoAleatorio(
    onClick: () -> Unit={}){

     OutlinedButton(onClick=onClick) {
         Text(text = "Aleatorio")
         Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null)
     }
}



@Composable
@Preview(showBackground = true)
fun previaDosBottoes(){
    Column {
        BotaoAleatorio()
    }
}