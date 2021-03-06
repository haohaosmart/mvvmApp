package com.wanghao.mvvmapp.ui.loginCompose


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import cn.com.woong.resultbacklib.ResultBack
import cn.com.woong.resultbacklib.ResultInfo
import com.jaeger.library.StatusBarUtil
import com.wanghao.mvvmapp.R
import com.wanghao.mvvmapp.mvvm.mviCore.observeEvent
import com.wanghao.mvvmapp.mvvm.widget.CommonPageTitle
import com.wanghao.mvvmapp.ui.loginMvi.FlowLoginActivity
import com.wanghao.mvvmapp.ui.loginMvi.LoginViewAction
import com.wanghao.mvvmapp.ui.loginMvi.LoginViewEvent
import com.wanghao.mvvmapp.ui.paging.PagingForUserActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComposeLoginActivity : ComponentActivity() {

    val TAG = "ComposeLogin"
    private val mViewModel by viewModels<ComposeLoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, start = 50.dp, end = 50.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    PageTitle()
                    Button(onClick = {
                        Log.d(TAG, "Timer started")
                        val intent = Intent(this@ComposeLoginActivity, PagingForUserActivity::class.java)
                        startActivity(intent)
                    }) {
                        Text("??????Paging3???Compose????????????")
                    }

                    Image(
                        painter = painterResource(R.mipmap.icon_logo),
                        contentDescription = "",
                        modifier = Modifier.size(width = 100.dp, height = 100.dp)
                    )
//                    AsyncImage(
//                        model = "https://img1.baidu.com/it/u=2500299038,2384650643&fm=253&fmt=auto&app=120&f=JPEG?w=660&h=328",
//                        contentDescription = null,
//                        modifier = Modifier.size(width = 100.dp, height = 100.dp)
//                    )
                    TimerTickerScreen()
//                    RequestPermission()
//                    testNormalActivityResult()
                    Spacer(modifier = Modifier.size(width = 0.dp, height = 100.dp))
                    AccountTextField()
                    Spacer(modifier = Modifier.size(width = 0.dp, height = 20.dp))
                    PasswordTextField()
                    Spacer(modifier = Modifier.size(width = 0.dp, height = 20.dp))
                    RoundedCornerCardComponent(buttonText = "??????")
                }
            }
        }

        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null)
        StatusBarUtil.setLightMode(this)

        initObserver()
    }


    @Composable
    fun PageTitle() {
        AndroidView(
            factory = {
                CommonPageTitle(it)
            },
            update = {
//                it.setTvTitle("????????????")
                it.setTvTitle("????????????")
            }
        )
    }

    fun initObserver() {
        // ?????????????????? ???????????????
        mViewModel.viewEvents.observeEvent(this) {
            when (it) {
                is LoginViewEvent.LoginRequestFinish -> {
                    Log.d(TAG, "----" + it.response.msg)
                }
                is LoginViewEvent.ShowLoadingDialog -> {
                    Log.d(TAG, "----showLoadingDialog----")
                }
                is LoginViewEvent.DismissLoadingDialog -> {
                    Log.d(TAG, "----dimissLoadingDialog----")
                }
            }
        }
    }


    @Composable
    fun AccountTextField() {
        TextField(value = mViewModel.viewStates.collectAsState().value.userName, onValueChange = {
            mViewModel.dispatch(LoginViewAction.UpdateUserName(it.toString()))
        }, label = { Text("??????") })
    }


    @Composable
    fun PasswordTextField() {
        TextField(value = mViewModel.viewStates.collectAsState().value.password, onValueChange = {
            mViewModel.dispatch(LoginViewAction.UpdatePassword(it))
        }, label = { Text("??????") })
    }


    @Composable
    fun LoginButton() {
        Button(
            onClick = {
                mViewModel.dispatch(LoginViewAction.Login)

                // ???????????????
//                mViewModel.dispatch(LoginViewAction.UpdateUserName("18920016040"))
//                mViewModel.dispatch(LoginViewAction.UpdatePassword("123123"))
            },
            elevation = ButtonDefaults.elevation(3.dp, 7.dp, 0.dp),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text("??????")
        }
    }


    @Composable
    fun RoundedCornerCardComponent(buttonText: String) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressState = interactionSource.collectIsPressedAsState()
        val backgroundColor = if (pressState.value) Color.Green else Color.Red
        val contentColor = if (pressState.value) Color.Yellow else Color.White
        val border =
            if (pressState.value) BorderStroke(1.dp, color = Color.Black) else BorderStroke(
                1.dp,
                color = Color.White
            )


        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(50.dp)
                .clickable(onClick = {
                    mViewModel.dispatch(LoginViewAction.Login)
                }),
            shape = RoundedCornerShape(8.dp),
            border = border,
            backgroundColor = backgroundColor
        ) {
            Text(
                text = buttonText,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = contentColor
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }


    @Composable
    fun RequestPermission() {
// ?????? LocalComposition ?????? Context
        val context = LocalContext.current

        // ?????? LocalLifecycleOwner ?????? Lifecycle
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        // ??????????????????????????? Permission ??????
        val permission = Manifest.permission.READ_PHONE_STATE

        // Result API ???????????? launcher
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                //????????????????????????????????????????????????????????????????????? onResult ???????????? @Composable lambda??????????????????????????? Composalbe ?????????????????? state ????????????????????? Composable
            }
        )

        // ??? Activity onStart ????????????????????????????????????????????????????????????
        val lifecycleObserver = remember {
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    if (!permission.isGrantedPermission(context)) {
                        launcher.launch(permission)
                    }
                }
            }
        }

        // ??? Lifecycle ?????? LifecycleObserver ?????????????????????????????? onDispose ??????????????????????????????
        DisposableEffect(lifecycle, lifecycleObserver) {
            lifecycle.addObserver(lifecycleObserver)
            onDispose {
                lifecycle.removeObserver(lifecycleObserver)
            }
        }
    }

    private fun String.isGrantedPermission(context: Context): Boolean {
        // ??????????????????????????????
        return context.checkSelfPermission(this) == PackageManager.PERMISSION_GRANTED
    }


    @Composable
    fun PressEffectButtonComponent(buttonText: String) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressState = interactionSource.collectIsPressedAsState()
        val backgroundColor = if (pressState.value) Color.Green else Color.Red
        val contentColor = if (pressState.value) Color.Yellow else Color.White
        val border =
            if (pressState.value) BorderStroke(1.dp, color = Color.Black) else BorderStroke(
                1.dp,
                color = Color.White
            )

        Button(
            onClick = {
                mViewModel.dispatch(LoginViewAction.Login)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            border = border,
            // contentColor????????????????????????  ?????????????????????
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                disabledBackgroundColor = Color.DarkGray,
                disabledContentColor = Color.Black
            ),
            interactionSource = interactionSource
        ) {
            Text(
                text = buttonText,
                textAlign = TextAlign.Center
            )
        }
    }


    @Composable
    fun TimerTickerScreen() {
        val scope = rememberCoroutineScope()
        val tickerText = remember {
            mutableStateOf(20)
        }

        Column {
            Button(onClick = {
                Log.d(TAG, "Timer started")
                scope.launch {
                    val timer = object: CountDownTimer(20000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            Log.d(TAG,"---onTick----" + millisUntilFinished)
                            tickerText.value--
                        }

                        override fun onFinish() {
                            Log.d(TAG,"---onFinish----")
                        }
                    }

                    timer.start()
                }
            }) {
                Text("Start Timer")
            }

            Text(tickerText.value.toString(), fontSize = 30.sp)
        }
    }




    // ????????????????????????rememberCoroutineScope?????????
    // ??????  https://proandroiddev.com/jetpack-compose-side-effects-ii-remembercoroutinescope-76104d7ff09
    // ??????  LaunchedEffect   ???Compose???????????????
    // ??????  https://blog.csdn.net/vitaviva/article/details/113832415
    @Composable
    fun TimerScreen() {
        val scope = rememberCoroutineScope()
        Column {
            Button(onClick = {
                Log.d(TAG, "Timer started")
                scope.launch {
                    try {
                        delay(5000)
                        Log.d(TAG,"-----timer end------")
                    }catch (ex:Exception){
                        Log.d(TAG,"------timer cancelled----")
                    }


                }
            }) {
                Text("Start Timer")
            }
            Button(onClick = {
                Log.d(TAG, "Cancel timer")
                scope.cancel()
            }) {
                Text("Cancel Timer")
            }
        }
    }


    // ??????????????????????????????
    @Composable
    fun testNormalActivityResult() {
        Button(onClick = {
            val intent = Intent(this@ComposeLoginActivity, FlowLoginActivity::class.java)
            ResultBack(this@ComposeLoginActivity).startForResult(intent, object : ResultBack.Callback {
                override fun onActivityResult(resultInfo: ResultInfo?) {
                    Log.d(TAG,"----onActivityResult----")
                }
            })

        }) {
            Text("???????????????onActivityResult??????")
        }
    }

}