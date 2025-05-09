package com.example.senemarketkotlin.ui.screens.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.ui.screens.chat.ChatScreen
import com.example.senemarketkotlin.ui.screens.favorites.FavoritesScreen
import com.example.senemarketkotlin.ui.screens.home.HomeScreen
import com.example.senemarketkotlin.ui.screens.profile.ProfileScreen
import com.example.senemarketkotlin.ui.screens.sell.SellScreen
import com.example.senemarketkotlin.ui.theme.Black
import com.example.senemarketkotlin.ui.theme.Gray
import com.example.senemarketkotlin.ui.theme.Gray2
import com.example.senemarketkotlin.ui.theme.Pink80
import com.example.senemarketkotlin.ui.theme.Yellow30
import com.example.senemarketkotlin.utils.NetworkConnectivityObserver
import com.example.senemarketkotlin.viewmodels.MainViewModel
import com.example.senemarketkotlin.viewmodels.SellScreenViewModel


data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

@Composable

fun MainScreen(navController: NavController,
               dataLayerFacade: DataLayerFacade, index: Int = 0 ) {

    val viewModel: MainViewModel = viewModel()
    val isConnected by viewModel.isConnected.collectAsState()

    val navItemList = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Outlined.Home
        ),
        BottomNavItem(
            label = "Chats",
            icon =  ImageVector.vectorResource(R.drawable.chat)

        ),
        BottomNavItem(
            label = "Sell",
            icon = ImageVector.vectorResource(R.drawable.upload)
        ),
        BottomNavItem(
            label = "Favorites",
            icon = ImageVector.vectorResource(R.drawable.ic_yellow_heart_outlined),

            ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Outlined.Person
        ),
    )

    var selectedIndex by remember {
        mutableIntStateOf(index)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = colorScheme.onPrimary,
                    tonalElevation = 5.dp,
                ) {
                    navItemList.forEachIndexed { idx, item ->
                        NavigationBarItem(
                            selected = selectedIndex == idx,
                            onClick = { selectedIndex = idx },
                            label = {
                                Text(text = item.label, fontSize = 11.sp)
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Yellow30,
                                unselectedIconColor = Black,
                                selectedTextColor = Gray,
                                unselectedTextColor = Black,
                                indicatorColor = Gray2,
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(
                modifier = Modifier.padding(innerPadding),
                selectedIndex = selectedIndex,
                navController = navController,
                dataLayerFacade = dataLayerFacade
            )
        }

        // Overlay de conexión en esquina superior izquierda
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp)
            .align(Alignment.TopEnd)) {
            NetworkStatusIndicator(isConnected)
        }
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier,
                  selectedIndex: Int,
                  navController: NavController,
                  dataLayerFacade: DataLayerFacade) {

    val sellScreenViewModel = SellScreenViewModel(navController, dataLayerFacade, LocalContext.current)

    when (selectedIndex) {

        0 -> HomeScreen(
            dataLayerFacade, navController
        )

        1 -> ChatScreen(

        )

        2 -> SellScreen(sellScreenViewModel

        )

        3 -> FavoritesScreen(dataLayerFacade, navController
        )

        4 -> ProfileScreen(dataLayerFacade, navController)

    }
}

@Composable
fun NetworkStatusIndicator(isConnected: Boolean) {
    if (!isConnected) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_wifi),
                contentDescription = "Sin conexión",
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Sin conexión",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}




