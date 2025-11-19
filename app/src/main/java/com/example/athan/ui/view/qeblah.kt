package com.example.athan.ui.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hassanjamil.hqibla.QiblaCompass
import com.hassanjamil.hqibla.hasLocationPermission
import com.hassanjamil.hqibla.rememberCompassAzimuth
import com.hassanjamil.hqibla.rememberLocationState
import com.hassanjamil.hqibla.rememberQiblaDirection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Qeblah() {

    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(context.hasLocationPermission()) }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            hasLocationPermission = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }

    val azimuthState = rememberCompassAzimuth()
    val locationState = rememberLocationState(hasLocationPermission)
    val qiblaDirection = rememberQiblaDirection(locationState.value).value


    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }
    if (hasLocationPermission)
        Scaffold(
            containerColor = Color.White,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Qeblah") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )

            }
        ) { innerPadding ->
            innerPadding.calculateTopPadding()
            innerPadding.calculateBottomPadding()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                QiblaCompass(
                    azimuthDegrees = azimuthState.value,
                    qiblaDirectionDegrees = qiblaDirection,
                    location = locationState.value,
                    animationDurationMillis = 260,
                    dialTint = Color.Transparent,
                    indicatorTint = Color.Black,
                    showInfoPanel = false
                )
            }
        }
}