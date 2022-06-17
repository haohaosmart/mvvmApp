package com.wanghao.mvvmapp.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.wanghao.mvvmapp.ui.paging.PagingForUserActivity

class DialogTestActivity : ComponentActivity() {
    val TAG = "DiaTest"

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
                    val showDialogValue = remember { mutableStateOf(true) }

                    Button(onClick = {
                        showDialogValue.value = true
                    }) {
                        Text("测试弹出对话框")
                    }

                    DialogTest(showDialogValue.value, onConfirmFunc = {
                        showDialogValue.value = false
                    }, onDismissFunc = {
                        showDialogValue.value = false
                    })
                    dropdownMenuTest()
                    PopupTest()
                }
            }
        }

    }

    @Composable
    fun PopupTest() {

        val expandState = remember {
            mutableStateOf(true)
        }
        if (expandState.value) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = {
                    Log.e("ccm", "执行了onDismissRequest")
                    expandState.value = false
                },
                offset = IntOffset(10, 10),
            ) {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .shadow(
                            elevation = 2.dp, shape = RoundedCornerShape(3.dp)
                        )
                        .background(Color.White, shape = RoundedCornerShape(3.dp))
                ) {
                    dropdownMenuItemTest(expandState, Icons.Filled.Favorite, "收藏")
                    dropdownMenuItemTest(expandState, Icons.Filled.Edit, "编辑")
                    dropdownMenuItemTest(expandState, Icons.Filled.Delete, "删除")
                }
            }
        }
    }

    @Composable
    fun DialogTest(
        showDialogValue: Boolean = false,
        onConfirmFunc: () -> Unit,
        onDismissFunc: () -> Unit
    ) {
        if (showDialogValue) {
            Dialog(
                onDismissRequest = {
                    Log.e("ccm", "====onDismiss=====")
                    onDismissFunc.invoke()
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    securePolicy = SecureFlagPolicy.SecureOff
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 15.dp)
                        .background(
                            Color.White, shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "对话框标题",
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "对话框内容,对话框内容,对话框内容,对话框内容,对话框内容,对话框内容",
                        modifier = Modifier.padding(horizontal = 10.dp),
                        lineHeight = 20.sp,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(modifier = Modifier.height(0.5.dp))
                    Row() {
                        Button(
                            onClick = {
                                onDismissFunc.invoke()
                            },
                            modifier = Modifier.weight(1f, true),
                            shape = RoundedCornerShape(bottomStart = 8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        ) {
                            Text(text = "取消")
                        }
                        Button(
                            onClick = {
                                onConfirmFunc.invoke()
                            },
                            modifier = Modifier.weight(1f, true),
                            shape = RoundedCornerShape(bottomEnd = 8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        ) {
                            Text(text = "确定")
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun dropdownMenuTest() {
        val expandState = remember {
            mutableStateOf(false)
        }
        Column() {
            Button(
                onClick = {
                    expandState.value = true
                }) {
                Text(text = "打开 DropdownMenu")
            }
            DropdownMenu(
                expanded = expandState.value,
                onDismissRequest = {
                    Log.e("ccm", "执行了onDismissRequest")
                    expandState.value = false
                },
                offset = DpOffset(10.dp, 10.dp),
                properties = PopupProperties()
            ) {
                dropdownMenuItemTest(expandState, Icons.Filled.Favorite, "收藏")
                dropdownMenuItemTest(expandState, Icons.Filled.Edit, "编辑")
                dropdownMenuItemTest(expandState, Icons.Filled.Delete, "删除")
            }
        }
    }


    @Composable
    fun dropdownMenuItemTest(state: MutableState<Boolean>, icon: ImageVector, text: String) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressState = interactionSource.collectIsPressedAsState()
        val focusState = interactionSource.collectIsFocusedAsState()
        DropdownMenuItem(
            onClick = {
                state.value = false
            },
            enabled = true,
            interactionSource = interactionSource
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (pressState.value || focusState.value) Color.Red else Color.Black
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 10.dp),
                color = if (pressState.value || focusState.value) Color.Red else Color.Black
            )
        }
    }

}



