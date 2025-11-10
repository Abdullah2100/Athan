package com.example.athan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.athan.ui.Navigation
import com.example.athan.ui.Screens
import com.example.athan.ui.theme.AthanTheme
import com.example.e_commercompose.model.ButtonNavItem

class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AthanTheme {
                val navController = rememberNavController()
                Scaffold(
                bottomBar = {
                    ApplicationButtonNavy(navController)
                }
                ) {
                    innerPadding->
                    innerPadding.calculateTopPadding()
                    innerPadding.calculateBottomPadding()

                    Navigation(navController)

                }

            }
        }
    }
}




@Composable
fun ApplicationButtonNavy(nav: NavHostController){


    val navBackStackEntry = nav.currentBackStackEntryAsState()


    val pages = listOf(
        Screens.HomeScreen,
        Screens.QeblahScreen,
        Screens.SettingScreen,
    )


    val buttonNavItemHolder = listOf(
        ButtonNavItem(
            name = "الرئيسية",
            imageId = Icons.Outlined.Home,

            ),
        ButtonNavItem(
            name = "القبلة",
            imageId =ImageVector.vectorResource(R.drawable.compass)
        ),
        ButtonNavItem(
            name = "الاعدادات",
            imageId = Icons.Outlined.Settings,

            ),

        )
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        0.2.dp
                    )
            )
            NavigationBar(
                modifier = Modifier.background(Color.White),
                tonalElevation = 5.dp,
                containerColor = Color.White,
                content = {
                    buttonNavItemHolder.fastForEachIndexed { index, value ->
                        val isSelectedItem =
                            navBackStackEntry.value?.destination?.hasRoute(
                                pages[index]::class
                            ) == true
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = Color.Black,
                                unselectedTextColor = Color.Gray,
                            ),

                            selected = isSelectedItem,
                            onClick = {
                                if (!isSelectedItem)
                                    nav.navigate(pages[index])
                            },
                            label = {
                                Text(
                                    text = value.name,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = value.imageId,
                                    contentDescription = "",
                                    modifier = Modifier.size(24.dp)
                                )
                            })
                    }

                }
            )
        }

}
