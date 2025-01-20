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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.componentes.BararInferior
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.Miniplayer
import com.example.songs.componentes.PermanenteNavigationDrawer
import com.example.songs.navegacao.DestinosDENavegacao
import com.example.songs.navegacao.Navgrafic

import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.example.songs.servicoDemidia.ServicMedia
import com.example.songs.ui.theme.SongsTheme
import com.example.songs.viewModels.FabricaMainViewmodel
import com.example.songs.viewModels.FabricaViewmodelPlyer
import com.example.songs.viewModels.MainViewModel
import com.example.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    var conecao =
        MutableStateFlow<ResultadosConecaoServiceMedia>(ResultadosConecaoServiceMedia.Desconectado)
    val scop = lifecycleScope
    val serviceConection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("service", "onServiceConnected")


            val binder = service as ServicMedia.ServicBinder
            conecao.value = ResultadosConecaoServiceMedia.Conectado(binder.getService(),this@MainActivity)
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
                val viewmodel: MainViewModel =
                    viewModel(factory = FabricaMainViewmodel().factory(conecao))
                val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
                val permissaoLeitura =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {it->

                            viewmodel.mudancaSolicitarPermicaoLaeitua(it)


                    }
                val permicaoNotificacao =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                       viewmodel.mudancaSolicitarPermicaoNotificao(it)
                    }

                val navController = rememberNavController()
                val scopMain = rememberCoroutineScope()
                val transicaoMiniPlyer = remember { MutableTransitionState(false) }
                val vieModelPlyers:VmodelPlayer=  viewModel(factory = FabricaViewmodelPlyer().fabricar(conecao))
                Surface {
                    LaunchedEffect(Unit) {
                        scopMain.launch {
                         checarPermicaoAudio(viewmodel)
                        checarPermicaoNotificacao(viewmodel)}}

                    Scaffold(topBar = { BarraSuperio(titulo = "Songs") },
                             bottomBar = {
                                 if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
                                     BararInferior(acaoNavegacao = {navController.navigate(it)})
                            else if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM)
                                     if(windowsizeclass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT)
                                           BararInferior(acaoNavegacao = {navController.navigate(it)})


                    },
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                            .safeGesturesPadding()
                            .safeContentPadding(),//PermanenteNavigationDrawer(acaoNavegacao = {navController.navigate(it)})
                        containerColor = MaterialTheme.colorScheme.background,
                        snackbarHost = { SnackbarHost(hostState = viewmodel.snackbarHostState) }) {

                        PermanentNavigationDrawer(drawerContent = {
                                                             if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED)
                                                                 PermanenteNavigationDrawer(acaoNavegacao = {navController.navigate(it)})
                                                     else   if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
                                                                 if(windowsizeclass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT)
                                                                     PermanenteNavigationDrawer(acaoNavegacao = {navController.navigate(it)})
                                                                 }
                                                     else{}
                                                                  },
                                                 modifier = Modifier.padding(paddingValues = it).fillMaxSize()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Navgrafic( navController = navController,
                                           windowSizeClass = windowsizeclass,
                                           modifier = Modifier.align(Alignment.TopCenter),
                                           paddingValues = it,
                                           transicaoMiniPlyer = transicaoMiniPlyer,
                                           vm = vieModelPlyers,
                                           acaoCaregarPlyer = {l,id->
                                               scop.launch {
                                                   vieModelPlyers.carregarLista(l,id)

                                                   vieModelPlyers.play()

                                               }
                                           },{viewmodel.mudarBigPlyer()}, estadoService = conecao)
                                val emreproducao =vieModelPlyers._emreproducao.collectAsState()
                                val bigPlyer =viewmodel._bigPlyer.collectAsState()
                                var funcao:()->Boolean ={
                                    if(bigPlyer.value && emreproducao.value){
                                        if(bigPlyer.value)Log.i("bigplyer","true")
                                        if(emreproducao.value)Log.i("emreproducao","true")
                                        true}
                                    else{
                                        if(!bigPlyer.value)Log.i("bigplyer","false")
                                        if(!emreproducao.value)Log.i("emreproducao","false")
                                        false}
                                }
                                AnimatedVisibility(visible =emreproducao.value,modifier = Modifier.align(Alignment.BottomCenter)) {
                                  if(!bigPlyer.value){  DisposableEffect(Unit) {
                                        scop.launch {
                                             transicaoMiniPlyer.targetState=true
                                        }
                                        onDispose {
                                            transicaoMiniPlyer.targetState=false
                                        }
                                    }
                                    Miniplayer(modifier = Modifier.align(Alignment.BottomCenter).clickable {
                                        scopMain.launch {
                                            transicaoMiniPlyer.targetState=false
                                        navController.navigate(DestinosDENavegacao.Player.rota)}
                                    },
                                    vm = vieModelPlyers,
                                    windoSizeClass = windowsizeclass)
                                  }
                                }
                                val dialigoLeitura = viewmodel.dialoLeitura.collectAsState(false)
                                if (dialigoLeitura.value) {
                                    AlertDialog(onDismissRequest = { scop.launch {} },
                                                title = { Text(text="Permicao para ler Dados do Dispositivo") },
                                                text = { Text(text = "A permicao e nessesaria para poder ler as musicas presentes no dispositivo ") },
                                                confirmButton = {
                                                         TextButton(onClick = {
                                                                 scop.launch {
                                                                     if(Build.VERSION.SDK_INT  <Build.VERSION_CODES.TIRAMISU)
                                                                         permissaoLeitura.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                                                     else permissaoLeitura.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
                                                                 }
                                                         }, content = { Text(text = "Ok") })
                                                }, dismissButton = {
                                                    TextButton(onClick = {scop.launch { viewmodel.mudancaSolicitarPermicaoLaeitua(false) }}, content = { Text(text = "Nao permitir") })
                                                       }
                                    )
                                }
                                val dialigoNotificacao =
                                    viewmodel.dialoNotificacao.collectAsState(false)
                                if (dialigoNotificacao.value) {
                                    AlertDialog(onDismissRequest = { scop.launch {} },
                                        title = { Text("permmicao de notificacao") },
                                        text = { Text(text = "A permicao e nessesaria para poder emitir o player de notificacao que permite controlar a musica em segundo plano") },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    permicaoNotificacao.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                                },
                                                content = { Text("ok") })
                                        }, dismissButton = {TextButton(onClick = {}, content = { Text(text = "Nao permitir") }) })
                                }
                            }
                        }
                   

                    }
                }
            }
        }
    }

   suspend fun checarPermicaoAudio(viewModel: MainViewModel){
       if(Build.VERSION.SDK_INT  <Build.VERSION_CODES.TIRAMISU)
           this.cehcarReadPermicion(viewModel)
       else this.checarReadMediaAudio(viewModel)
   }

   suspend fun cehcarReadPermicion(viewModel: MainViewModel){
     if (ContextCompat.checkSelfPermission(
           this@MainActivity,
           android.Manifest.permission.READ_EXTERNAL_STORAGE
       ) != android.content.pm.PackageManager.PERMISSION_GRANTED
               )
       viewModel.mudarPermicaoLeitura(false)
       else viewModel.mudarPermicaoLeitura(true)
   }

   suspend fun checarReadMediaAudio(viewModel: MainViewModel){
       if (ContextCompat.checkSelfPermission(
               this@MainActivity,
               android.Manifest.permission.READ_MEDIA_AUDIO
           ) != android.content.pm.PackageManager.PERMISSION_GRANTED
       )viewModel.mudarPermicaoLeitura(false)
       else viewModel.mudarPermicaoLeitura(true)
   }

   suspend fun checarPermicaoNotificacao(viewModel: MainViewModel){
       if (ContextCompat.checkSelfPermission(
               this@MainActivity,
               android.Manifest.permission.POST_NOTIFICATIONS
           ) != android.content.pm.PackageManager.PERMISSION_GRANTED
       )
           viewModel.mudarPermicaoNotificacao(false)
       else viewModel.mudarPermicaoNotificacao(value = true)
   }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()



        val i = Intent(this@MainActivity, ServicMedia::class.java)
        startForegroundService(i)
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
        when(val r =conecao.value){
            is ResultadosConecaoServiceMedia.Conectado->{
               try {
                   unbindService(serviceConection)
               }catch (e:Exception){
                   Log.e("main",e.toString())
                   conecao.value=ResultadosConecaoServiceMedia.Desconectado
               }

            }
            else->{}
        }

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