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
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.State
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.android.gms.ads.MobileAds
import com.songsSongs.songs.componentes.Banner
import com.songsSongs.songs.componentes.BararInferior
import com.songsSongs.songs.componentes.BarraSuperio
import com.songsSongs.songs.componentes.Miniplayer
import com.songsSongs.songs.componentes.PermanenteNavigationDrawer
import com.songsSongs.songs.componentes.dialog.DialogoNotificacoes
import com.songsSongs.songs.componentes.dialog.DialogoPermicaoLeituraDasMusicas
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        observadorDocicloDeVida = HelperLifeciclerObserver(acaoDeConectar = {
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
                val permissaoLeitura = remenberRequisicaoLeitura(viewmodel)


                val permicaoNotificacao = remenberRequisicaoNotificacao(viewmodel)


                val navController = rememberNavController()
                val scopMain = rememberCoroutineScope()
                val transicaoMiniPlyer = remember { MutableTransitionState(false) }
                val vieModelPlyers:VmodelPlayer=  viewModel(factory = FabricaViewmodelPlyer().fabricar(conecao))
                val emreproducao =vieModelPlyers._emreproducao.collectAsState()
                val bigPlyer =viewmodel._bigPlyer.collectAsState()
                val corBackGround =viewmodel._corBackGround.collectAsState()
                val dialigoLeitura = viewmodel.dialoLeitura.collectAsState(false)
                val dialigoNotificacao =viewmodel.dialoNotificacao.collectAsState(false)
                Surface(modifier = Modifier) {
                    LaunchedEffect(Unit) {scopMain.launch(Dispatchers.Default) {
                                          checarPermicaoAudio(viewmodel)
                                          checarPermicaoNotificacao(viewmodel)}}

                    Scaffold(topBar = {TopBarMAin(bigPlyer) },
                             bottomBar = { BottonBarMain(windowsizeclass,bigPlyer,navController) },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(corBackGround.value)
                            .safeDrawingPadding()
                            .safeGesturesPadding()
                            .safeContentPadding()
                            .imePadding(),
                        containerColor = MaterialTheme.colorScheme.background,
                        snackbarHost = { SnackbarHost(hostState = viewmodel.snackbarHostState) }) {

                        PermanentNavigationDrawer(drawerContent = {COnteudoDrawer(viewmodel,windowsizeclass,navController,corBackGround)},//
                                                  modifier = Modifier.fillMaxSize().padding(paddingValues = if (!bigPlyer.value) it else PaddingValues(0.dp))) {


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
                                                   vieModelPlyers.play()}},
                                          acaoAvisoBigplyer = {
                                              scopMain.launch(Dispatchers.Default) {
                                                  delay(1000)
                                                  if(!it) viewmodel.mudarCorBackGround(Color.Unspecified)
                                                  viewmodel.mudarBigPlyer(it)}
                                              },
                                          acaoMudaBackgraundScafolld = {viewmodel.mudarCorBackGround(it)},
                                          acaoMudarcorBackgrandEBarraPermanent = {b,c-> viewmodel.mudarCorBackGroundEtexto(b,c)},
                                          estadoService = conecao,
                                          acaOcultarBaras = {windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars())},
                                          acaOnMostraBaras = {
                                              scopMain.launch {
                                                  delay(500)
                                              windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars())
                                              }
                                          })

                                MiniPlyerMain(boxScope = this,
                                              emreproducao=emreproducao,
                                              bigPlyer=bigPlyer,
                                              transicaoMiniPlyer=transicaoMiniPlyer,
                                              scopMain=scopMain, viewmodel = viewmodel, vieModelPlyers = vieModelPlyers,
                                              windowSizeClass = windowsizeclass,
                                              windowInsetsControllerCompat = windowInsetsControllerCompat,
                                              acaoNavegacao = { navController.navigate(DestinosDENavegacao.DestinosDeTela.Player)})
                                ApresentacaoBaner(this,emreproducao,bigPlyer,transicaoMiniPlyer)
                               DialogoPermicaoLeituraDasMusicas(dialogoDeLeitura =dialigoLeitura,
                                                    viewmodel = viewmodel,
                                                    scope = scopMain,
                                                    acaoPermicaoNotificacaoSDKInferiorATiramisu = {permissaoLeitura.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)},
                                                    acaoPermicaoNotificacaoSDKTiramisuESuperiores =  {permissaoLeitura.launch(android.Manifest.permission.READ_MEDIA_AUDIO)})

                               DialogoNotificacoes(dialigoNotificacao = dialigoNotificacao,
                                                   acaoPermicaoNotificacao = {permicaoNotificacao.launch(android.Manifest.permission.POST_NOTIFICATIONS)},
                                                   viewmodel = viewmodel, scope = scopMain)

                            }
                        }}
                }
            }
        }
    }


    @Composable
    fun remenberRequisicaoNotificacao(viewmodel: MainViewModel)= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        if(it&&!viewmodel._permicaoNotificacao.value){
            val i = Intent(this@MainActivity, ServicMedia::class.java)
            startForegroundService(i)
            val i1 = Intent(this@MainActivity, ServicMedia::class.java)
            bindService(i1,serviceConection,BIND_IMPORTANT)
        }
        viewmodel.mudancaSolicitarPermicaoNotificao(it)
    }
    @Composable
    fun remenberRequisicaoLeitura(viewmodel: MainViewModel)= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {it->
        Log.i("permissao", "permicao de leitura ${it.toString()}")
        viewmodel.mudancaSolicitarPermicaoLaeitua(it)}

    @Composable
    fun TopBarMAin(bigPlyer: State<Boolean>){
        AnimatedVisibility(visible = !bigPlyer.value){BarraSuperio(titulo = "Songs") } }
    @Composable
    fun BottonBarMain(windowsizeclass: WindowSizeClass,bigPlyer:State<Boolean>,navController: NavController){
        if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT)
            AnimatedVisibility(visible = !bigPlyer.value) { BararInferior(acaoNavegacao = {navController.navigate(route = it){launchSingleTop=true}})}
        else if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM)
            if(windowsizeclass.windowHeightSizeClass!=WindowHeightSizeClass.COMPACT)
                AnimatedVisibility(visible = !bigPlyer.value) {BararInferior(acaoNavegacao = {navController.navigate(it){launchSingleTop=true} }) }}


    @Composable
    fun MiniPlyerMain(boxScope: BoxScope,
                      emreproducao:State<Boolean>,
                      bigPlyer:State<Boolean>,
                      transicaoMiniPlyer: MutableTransitionState<Boolean>,
                      viewmodel: MainViewModel,
                      vieModelPlyers:VmodelPlayer,
                      windowSizeClass:WindowSizeClass,scopMain:CoroutineScope,
                      windowInsetsControllerCompat: WindowInsetsControllerCompat,acaoNavegacao:()->Unit={}){
        with(boxScope){
        AnimatedVisibility(visible =emreproducao.value,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(10.dp),) {
        if(!bigPlyer.value)
        {
            DisposableEffect(Unit) {
                scopMain.launch {
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

                        val comclusao = scopMain.async {

                            viewmodel.mudarBigPlyer(true)
                            transicaoMiniPlyer.targetState = false
                            windowInsetsControllerCompat.hide(
                                WindowInsetsCompat.Type.systemBars()
                            )
                            //delay(200)
                        }
                        comclusao.await()
                        //navController.navigate(DestinosDENavegacao.DestinosDeTela.Player)
                        acaoNavegacao()

                    }

                },
                vm = vieModelPlyers,
                windoSizeClass = windowSizeClass)
        }


    }}
    }

   @Composable
   fun COnteudoDrawer(viewmodel: MainViewModel,
                      windowsizeclass: WindowSizeClass,
                      navController: NavController,
                      corBackGround:State<Color> ){
          val cor =viewmodel._corDotextonoAppBar.collectAsState()
           if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.EXPANDED)
           PermanenteNavigationDrawer(modifier = Modifier.background(corBackGround.value),
               acaoNavegacao = {navController.navigate(it){launchSingleTop=true} },
               cor = cor.value,
               corBackgrand = corBackGround.value)
    else   if(windowsizeclass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM){
           if(windowsizeclass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT)
               PermanenteNavigationDrawer(modifier = Modifier.background(corBackGround.value),
                   acaoNavegacao = {navController.navigate(it){ launchSingleTop=true } },
                   cor = cor.value,
                   corBackgrand = corBackGround.value)}}



    @Composable
    fun ApresentacaoBaner(boxScope: BoxScope,emreproducao:State<Boolean>,
                          bigPlyer:State<Boolean>,
                          transicaoMiniPlyer: MutableTransitionState<Boolean>){
       with(boxScope) {
            AnimatedVisibility(visible = (!emreproducao.value&&!bigPlyer.value),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(10.dp)) {
        if(!transicaoMiniPlyer.targetState)
            Row(Modifier.align(Alignment.BottomCenter)) {Banner()}}}
    }




    fun modoImersivo(context: Context):WindowInsetsControllerCompat{
        val windowInsentsControler= WindowInsetsControllerCompat(window,window.decorView)
        windowInsentsControler.systemBarsBehavior=WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    return windowInsentsControler
    }
    //aqui eu manipulo a cor de status bar e navigation bar do sistema android deixei ambos transparentes
    fun ComponentActivity.enableEdgeToEdge(
      statusBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT),
      navigationBar:SystemBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT,android.graphics.Color.TRANSPARENT)) {
}

   suspend fun checarPermicaoAudio(viewModel: MainViewModel){
       if(Build.VERSION.SDK_INT  <Build.VERSION_CODES.TIRAMISU)
           this.cehcarReadPermicion(viewModel)
       else this.checarReadMediaAudio(viewModel)
   }

   suspend fun cehcarReadPermicion(viewModel: MainViewModel){
     if (ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED)
       viewModel.mudarPermicaoLeitura(false)
     else viewModel.mudarPermicaoLeitura(true)
   }

   suspend fun checarReadMediaAudio(viewModel: MainViewModel){
       if (ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.READ_MEDIA_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED){
            viewModel.mudarPermicaoLeitura(false)
            return }
       viewModel.mudarPermicaoLeitura(true)
   }

   suspend fun checarPermicaoNotificacao(viewModel: MainViewModel){
       if (ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED){
           viewModel.mudarPermicaoNotificacao(false)
       return }
       viewModel.mudarPermicaoNotificacao(value = true)
   }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()}






}

