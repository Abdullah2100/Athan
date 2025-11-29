package com.example.athan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.athan.worker.UpdateAthanWorder
import com.example.athan.ui.Navigation
import com.example.athan.ui.Screens
import com.example.athan.ui.theme.AthanTheme
import com.example.athan.viewModel.AthanViewModel
import com.example.athan.viewModel.ConnectivityViewModel
import com.example.athan.viewModel.LocaleViewModel
import com.example.e_commercompose.model.ButtonNavItem
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val athanViewModel: AthanViewModel by viewModels()
    private val localeViewModel: LocaleViewModel by viewModels()
    private val connectivityViewModel: ConnectivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localeViewModel.updateContext(this@MainActivity)
        localeViewModel.setDefaultLocale("en")


        lifecycleScope.launch {
            localeViewModel.savedLocale.collect{ value->
                if(value!=null){
                    localeViewModel.updateLocale(value.name)
                    localeViewModel.whenLanguageUpdateDo(value.name)
                    return@collect
                }
            }
        }


        val workManager = WorkManager.getInstance(this)
        val nextWork = PeriodicWorkRequestBuilder<UpdateAthanWorder>(1, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "UpdateAthanWork",
            ExistingPeriodicWorkPolicy.KEEP,
            nextWork
        )


        // Check and request battery optimization exemption for reliable alarms

        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            val locationClient = LocationServices.getFusedLocationProviderClient(this)

            val isAlreadyHasSavedLocation = athanViewModel.isSavedUserLocation.collectAsState()
            val isThereAthanAtDb = athanViewModel.isThereSavedAthanDates.collectAsState()
            val currentLocale = localeViewModel.savedLocale.collectAsState()


            val updateDirection = remember {
                derivedStateOf {
                    if (currentLocale.value?.name == "ar") {
                        LayoutDirection.Rtl

                    } else {
                        LayoutDirection.Ltr
                    }
                }
            }

            val requestLocationPermission = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val arePermissionsGranted =
                        permissions.values.reduce { acc, next -> acc && next }

                    if (arePermissionsGranted) {
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return@rememberLauncherForActivityResult
                        }


                        locationClient.lastLocation.apply {
                            addOnSuccessListener { location ->
                                if (location != null) {
                                    if (isThereAthanAtDb.value == null || isThereAthanAtDb.value == false)
                                        athanViewModel.getAthanDates(
                                            location.latitude,
                                            location.longitude
                                        )

                                    athanViewModel.saveUserLocationToDB(
                                        location.latitude,
                                        location.longitude
                                    )

                                }
                            }
                            addOnFailureListener { fail ->
                                Log.d(
                                    "contextError",
                                    "the current location is null ${fail.stackTrace}"
                                )

                            }
                        }


                        // Got last known location. In some  situations this can be null.
                    } else {
                        Toast.makeText(
                            this,
                            "You should give app permission to start the application",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })



            LaunchedEffect(Unit) {
                if (isAlreadyHasSavedLocation.value == false) {
                    val permissionList =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.SCHEDULE_EXACT_ALARM,
                            )
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.SCHEDULE_EXACT_ALARM,
                            )
                        else
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                            )

                    requestLocationPermission.launch(permissionList)
                } else {
                    athanViewModel.getAthanDates()
                }
            }







            AthanTheme {
                CompositionLocalProvider(
                    LocalLayoutDirection provides updateDirection.value
                ) {
                    Scaffold(
                        containerColor = Color.White,
                        bottomBar = {
                            ApplicationButtonNavy(navController)
                        }
                    ) { innerPadding ->
                        innerPadding.calculateTopPadding()
                        innerPadding.calculateBottomPadding()

                        Navigation(
                            navController,
                            athanViewModel,
                            localeViewModel,
                            connectivityViewModel
                        )

                    }
                }

            }
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun ApplicationButtonNavy(nav: NavHostController) {


    val navBackStackEntry = nav.currentBackStackEntryAsState()


    val pages = listOf(
        Screens.HomeScreen,
        Screens.QeblahScreen,
        Screens.SettingScreen,
    )


    val buttonNavItemHolder = listOf(
        ButtonNavItem(
            name = R.string.home,
            imageId = Icons.Outlined.Home,

            ),
        ButtonNavItem(
            name =R.string.qeblah,
            imageId = ImageVector.vectorResource(R.drawable.compass)
        ),
        ButtonNavItem(
            name = R.string.setting,
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
                                text =stringResource(value.name),
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
