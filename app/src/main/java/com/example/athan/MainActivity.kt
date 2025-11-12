package com.example.athan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.app.ActivityCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.athan.services.TimeBroadcasts
import com.example.athan.ui.Navigation
import com.example.athan.ui.Screens
import com.example.athan.ui.theme.AthanTheme
import com.example.athan.viewModel.AthanViewModel
import com.example.e_commercompose.model.ButtonNavItem
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 private  val athanViewMoldel: AthanViewModel by viewModels()


    override fun onStart() {
        val timerBoardCast = TimeBroadcasts();
        val intentFLag = IntentFilter(Intent.ACTION_TIME_TICK)
        registerReceiver(timerBoardCast,intentFLag)
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {



            val locationClient = LocationServices.getFusedLocationProviderClient(this)

            val isAlreadyHasSavedLocation =  athanViewMoldel.isSavedUserLocation.collectAsState()
            val isNeedToGetAthansFromApi =  athanViewMoldel.isThereSavedAthanDates.collectAsState()



            val requestLocationPermission = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(), onResult = { permissions ->
                    val arePermissionsGranted = permissions.values.reduce { acc, next -> acc && next }

                    if (arePermissionsGranted) {
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return@rememberLauncherForActivityResult
                        }

                        locationClient.lastLocation.apply {
                            addOnSuccessListener { location ->

                                if(isNeedToGetAthansFromApi.value==null)
                                    athanViewMoldel.getAthanDates(location?.latitude?:15.3522,location?.longitude?:44.2095);

                                athanViewMoldel.saveUserLocationToDB(location?.latitude?:15.3522,location?.longitude?:44.2095);


                            }
                            addOnFailureListener { fail ->
                                Log.d(
                                    "contextError", "the current location is null ${fail.stackTrace}"
                                )

                            }
                        }


                        // Got last known location. In some srare situations this can be null.
                    } else {
                        Toast.makeText(
                            this,
                            "You should give app permission to start the application",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })




            LaunchedEffect(Unit) {
                if(isAlreadyHasSavedLocation.value==false){
                    requestLocationPermission.launch(arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ))
                }else {
                    athanViewMoldel.getAthanDates();
                }
            }

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

                    Navigation(navController,athanViewMoldel)

                }

            }
        }
    }
}




@SuppressLint("SuspiciousIndentation")
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
                    .background(Color.Gray)
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
