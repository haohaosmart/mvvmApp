package com.wanghao.mvvmapp.ui.navTab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wanghao.mvvmapp.R


class NavTabActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController)}) {
        NavHost(navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                HomeScreen()
            }
            composable(NavigationItem.Music.route) {
                MusicScreen()
            }
            composable(NavigationItem.Movies.route) {
                MoviesScreen()
            }
            composable(NavigationItem.Mine.route) {
                MineScreen()
            }
        }
    }
}



@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(NavigationItem.Home, NavigationItem.Music, NavigationItem.Movies, NavigationItem.Mine)
    BottomNavigation(backgroundColor = Color.White, contentColor = colorResource(
        id = R.color.colorPrimary
    )) {
        val navBackStackEntry by navController.currentBackStackEntryFlow.collectAsState(initial =navController.currentBackStackEntry )
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach {
//            navigationItem -> NavigationItemView(navigationItem = navigationItem)
            item ->  BottomNavigationItem(
            icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
            label = { Text(text = item.title) },
            selectedContentColor = colorResource(
                id = R.color.colorPrimary
            ),
            unselectedContentColor = colorResource(
                id = R.color.colorUnselected
            ),
            alwaysShowLabel = true,
            selected = currentRoute == item.route,
            onClick = {
                navController.navigate(item.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        )
        }
    }
}






@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        Text(text = "This is Home")
    }
}

@Composable
fun MusicScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        Text(text = "This is Music")
    }
}

@Composable
fun MoviesScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        Text(text = "This is Movies")
    }
}
@Composable
fun MineScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        Text(text = "This is mine")
    }
}

sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    object Home: NavigationItem("home", R.mipmap.meicon_mine, "Home")
    object Music: NavigationItem("music", R.mipmap.meicon_mine, "Music")
    object Movies: NavigationItem("movies", R.mipmap.meicon_mine, "Movie")
    object Mine: NavigationItem("mine", R.mipmap.meicon_mine, "Mine")
}
