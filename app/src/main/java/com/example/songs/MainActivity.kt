package com.example.songs

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.songs.navegacao.Navgrafic

import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.servicoDemidia.ServicMedia
import com.example.songs.ui.theme.SongsTheme
import com.example.songs.viewModels.FabricaMainViewmodel
import com.example.songs.viewModels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    var conecao =
        MutableStateFlow<ResultadosConecaoServiceMedia>(ResultadosConecaoServiceMedia.Desconectado)
    val scop = lifecycleScope
    val serviceConection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("service", "onServiceConnected")


            val binder = service as ServicMedia.ServicBinder
            conecao.value = ResultadosConecaoServiceMedia.Conectado(binder.getService())
            Log.i("sevice", "onServiceConnected ${conecao.value}")


        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
            scop.launch(Dispatchers.Main) {
                conecao.emit(ResultadosConecaoServiceMedia.Erro("erro ao conectar bind e null"))
            }

        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            scop.launch(Dispatchers.Main) {
                conecao.emit(ResultadosConecaoServiceMedia.Erro("erro ao conectar bind e died"))
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("sevice", "onServiceDisconnected")
            scop.launch(Dispatchers.Main) {
                conecao.emit(ResultadosConecaoServiceMedia.Desconectado)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SongsTheme {

                val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
                val permissaoLeitura =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

                    }
                val permicaoNotificacao =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {

                    }
                val viewmodel: MainViewModel =
                    viewModel(factory = FabricaMainViewmodel().factory(conecao))
                val navController = rememberNavController()
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .safeContentPadding()
                    .safeDrawingPadding()
                    .safeGesturesPadding(),
                    snackbarHost = { SnackbarHost(hostState = viewmodel.snackbarHostState) }) {
                    Navgrafic(
                        navController = navController,
                        windowSizeClass = windowsizeclass,
                        paddingValues = it
                    )
                }


            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()


        val executor = Executors.newSingleThreadExecutor()
        val i = Intent(this@MainActivity, ServicMedia::class.java)
        startService(i)
        when(conecao.value){
            is ResultadosConecaoServiceMedia.Conectado->{}
            is ResultadosConecaoServiceMedia.Desconectado->{
                val i = Intent(this@MainActivity, ServicMedia::class.java)
                bindService(i,serviceConection,BIND_IMPORTANT)

            }
            is ResultadosConecaoServiceMedia.Erro->{
                val i = Intent(this@MainActivity, ServicMedia::class.java)
                bindService(i,serviceConection,BIND_IMPORTANT)
            }

        }



    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()



    }

    override fun onPause() {

        scop.launch(Dispatchers.Main) {
        unbindService(serviceConection)}
        super.onPause()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SongsTheme {
        Greeting("Android")
    }
}