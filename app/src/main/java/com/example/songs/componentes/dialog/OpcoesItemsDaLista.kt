package com.example.songs.componentes.dialog

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.DialogHost


@Composable
fun ItemsDialogoOpcoesItems(modifier:Modifier=Modifier,item:ObjetosDeDialog ) {

    Row(modifier = modifier.fillMaxWidth())   {
        Text(text = item.opcao)
        Spacer(modifier=Modifier.padding(10.dp))
        if(item.icone!=null) Icon(painter = painterResource(id = item.icone), contentDescription = null)
        else if (item.icones!=null) Icon(imageVector = item.icones, contentDescription = null)

    }
}