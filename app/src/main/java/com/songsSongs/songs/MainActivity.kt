package com.songsSongs.songs

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.songsSongs.songs.componentes.Banner
import com.songsSongs.songs.componentes.BararInferior
import com.songsSongs.songs.componentes.BarraSuperio
import com.songsSongs.songs.componentes.Miniplayer
import com.songsSongs.songs.componentes.PermanenteNavigationDrawer
import com.songsSongs.songs.navegacao.DestinosDENavegacao
import com.songsSongs.songs.navegacao.Navgrafic

import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import com.songsSongs.songs.servicoDemidia.ServicMedia
import com.songsSongs.songs.ui.theme.SongsTheme
import com.songsSongs.songs.viewModels.FabricaMainViewmodel
import com.songsSongs.songs.viewModels.FabricaViewmodelPlyer
import com.songsSongs.songs.viewModels.HelperLifeciclerObserver
import com.songsSongs.songs.viewModels.MainViewModel
import com.songsSongs.songs.viewModels.VmodelPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
    lateinit var observadorDocicloDeVida: HelperLifeciclerObserver

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     enableEdgeToEdge()
        observadorDocicloDeVida = HelperLifeciclerObserver(
            acaoDeConectar = {
                scop.launch(Dispatchers.IO) {
              MobileAds.initialize(this@MainActivity)

            }
                scop.launch(Dispatchers.Main){
                when(conecao.value){
                is ResultadosConecaoServiceMedia.Conectado->{}
                is ResultadosConecaoServiceMedia.Desconectado->{
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ){
                        val i = Intent(this@MainActivity, ServicMedia::class.java)
                        startForegroundService(i)
                        val i1 = Intent(this@MainActivity, ServicMedia::class.java)
                        bindService(i1,serviceConection,BIND_IMPORTANT)}

                }
                is ResultadosConecaoServiceMedia.Erro->{
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                    ){
                        val i = Intent(this@MainActivity, ServicMedia::class.java)
                        startForegroundService(i)
                        val i1 = Intent(this@MainActivity, ServicMedia::class.java)
                        bindService(i1,serviceConection,BIND_IMPORTANT)}
                }

            }
                }

                             },
            acaoDeDesconectar ={

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
            }},
            acaoChecagemConecao ={
                when(conecao.value){
                    is ResultadosConecaoServiceMedia.Conectado->{}
                    is ResultadosConecaoServiceMedia.Desconectado->{
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                android.Manifest.permission.POST_NOTIFICATIONS
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        ){
                            val i = Intent(this@MainActivity, ServicMedia::class.java)
                            startForegroundService(i)
                            val i1 = Intent(this@MainActivity, ServicMedia::class.java)
                            bindService(i1,serviceConection,BIND_IMPORTANT)}

                    }
                    is ResultadosConecaoServiceMedia.Erro->{
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                android.Manifest.permission.POST_NOTIFICATIONS
                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        ){
                            val i = Intent(this@MainActivity, ServicMedia::class.java)
                            startForegroundService(i)
                            val i1 = Intent(this@MainActivity, ServicMedia::class.java)
                            bindService(i1,serviceConection,BIND_IMPORTANT)}
                    }

                }
            })
        this.lifecycle.addObserver(observadorDocicloDeVida)
        val windowInsetsControllerCompat=modoImersivo(this)






        setContent {
            SongsTheme {
                val viewmodel: MainViewModel =
                    viewModel(factory = FabricaMainViewmodel().factory(conecao))
                val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
                val permissaoLeitura =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {it->
                       viewmodel.mudancaSolicitarPermicaoLaeitua(it)}

                val permicaoNotificacao =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
                        if(it&&!viewmodel._permicaoNotificacao.value){
                            val i = Intent(this@MainActivity, ServicMedia::class.java)
                            startForegroundService(i)
                            val i1 = Intent(this@MainActivity, ServicMedia::class.java)
                            bindService(i1,serviceConection,BIND_IMPORTANT)
                        }
                       viewmodel.mudancaSolicitarPermicaoNotificao(it)
                    }

                val navController = rememberNavController()
                val scopMain = rememberCoroutineScope()
                val transicaoMiniPlyer = remember { MutableTransitionState(false) }
                val vieModelPlyers:VmodelPlayer=  viewModel(factory = FabricaViewmodelPlyer().fabricar(conecao))
                val emreproducao =vieModelPlyers._emreproducao.collectAsState()
                val bigPlyer =viewmodel._bigPlyer.collectAsState()
                val corBackGround =viewmodel._corBackGround.collectAsState()

                Surface(modifier = Modifier) {
                    LaunchedEffect(Unit) {
                        scopMain.launch {
                         checarPermicaoAudio(viewmodel)
                        checarPermicaoNotificacao(viewmodel)}}

                    Scaffold(topBar = { AnimatedVisibility(visible = !bigPlyer.value){BarraSuperio(titulo = "Songs") } },
                             bottomBar = {
                                 if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
                                   AnimatedVisibility(visible = !bigPlyer.value) { BararInferior(acaoNavegacao = {
                                      // navController.popBackStack()
                                       navController.navigate(route = it){
                                       launchSingleTop=true
                                   }
                                   })}

                            else if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM)
                                     if(windowsizeclass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT)
                                         AnimatedVisibility(visible = !bigPlyer.value) {BararInferior(acaoNavegacao = {
                                           //  navController.popBackStack()
                                             navController.navigate(it){launchSingleTop=true} }) }


                    },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(corBackGround.value)
                            .safeDrawingPadding()
                            .safeGesturesPadding()
                            .safeContentPadding()
                            .imePadding(),//PermanenteNavigationDrawer(acaoNavegacao = {navController.navigate(it)})
                        containerColor = MaterialTheme.colorScheme.background,
                        snackbarHost = { SnackbarHost(hostState = viewmodel.snackbarHostState) }) {

                        PermanentNavigationDrawer(drawerContent = {
                            val cor =viewmodel._corDotextonoAppBar.collectAsState()
                                                             if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED)

                                                                 PermanenteNavigationDrawer(modifier = Modifier.background(corBackGround.value),
                                                                                            acaoNavegacao = {navController.navigate(it){
                                                                     launchSingleTop=true
                                                                 } },cor = cor.value,corBackgrand = corBackGround.value)
                                                     else   if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
                                                                 if(windowsizeclass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT)
                                                                     PermanenteNavigationDrawer(modifier = Modifier.background(corBackGround.value),
                                                                                               acaoNavegacao = {navController.navigate(it){
                                                                         launchSingleTop=true
                                                                     } },cor = cor.value,corBackgrand = corBackGround.value)
                                                                 }
                                                     else{}
                                                                  },//
                                                 modifier = Modifier
                                                     .fillMaxSize()
                                                     .padding(
                                                         paddingValues = if (!bigPlyer.value) it else PaddingValues(
                                                             0.dp
                                                         )
                                                     )) {
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
                                           }, acaoAvisoBigplyer = {
                                               viewmodel.mudarCorBackGround(Color.Unspecified)
                                               viewmodel.mudarBigPlyer()
                                                              },acaoMudaBackgraundScafolld = {
                                               Log.i("corbackgraund scafolld",it.toString())
                                               viewmodel.mudarCorBackGround(it)},
                                           acaoMudarcorBackgrandEBarraPermanent = {b,c-> viewmodel.mudarCorBackGroundEtexto(b,c)},
                                    estadoService = conecao,
                                    acaOcultarBaras = {windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())},
                                    acaOnMostraBaras = {windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())})


                                AnimatedVisibility(visible =emreproducao.value,
                                                   modifier = Modifier
                                                       .align(Alignment.BottomCenter)
                                                       .padding(10.dp),) {
                                  if(!bigPlyer.value)
                                  {
                                      DisposableEffect(Unit) {
                                        scop.launch {
                                             transicaoMiniPlyer.targetState=true
                                        }
                                        onDispose {
                                            transicaoMiniPlyer.targetState=false
                                        }
                                    }
                                    Miniplayer(modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            scopMain.launch {
                                                scopMain.launch {
                                                    val comclusao = scopMain.async {

                                                        viewmodel.mudarBigPlyer()
                                                        transicaoMiniPlyer.targetState = false
                                                        windowInsetsControllerCompat.hide(
                                                            WindowInsetsCompat.Type.systemBars()
                                                        )
                                                        delay(200)
                                                    }
                                                    comclusao.await()
                                                    navController.navigate(DestinosDENavegacao.DestinosDeTela.Player)
                                                }
                                            }

                                        },
                                    vm = vieModelPlyers,
                                    windoSizeClass = windowsizeclass)
                                  }


                                }
                                AnimatedVisibility(visible = (!emreproducao.value&&!bigPlyer.value),
                                                   modifier = Modifier.align(Alignment.BottomCenter)
                                                                      .padding(10.dp
                                                                      )) {
                                    if(!transicaoMiniPlyer.targetState)
                                        Row(Modifier.align(Alignment.BottomCenter)) {
                                            Banner()
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
                                        }, dismissButton = {TextButton(onClick = {viewmodel.mudancaSolicitarPermicaoNotificao(false)}, content = { Text(text = "Nao permitir") }) })
                                }

                            }
                        }



                    }
                }
            }
        }
    }

    fun modoImersivo(context: Context):WindowInsetsControllerCompat{

        val windowInsentsControler= WindowInsetsControllerCompat(window,window.decorView)
        windowInsentsControler.systemBarsBehavior=WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    return windowInsentsControler
    }
    fun ComponentActivity.enableEdgeToEdge(//aqui eu manipulo a cor de status bar e navigation bar do sistema android deixei ambos transparentes
    statusBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)
    ,navigationBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)) {
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
        super.onStart()}

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
}

    override fun onPause() {
       super.onPause()
    }

    override fun onDestroy() {

        super.onDestroy()
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