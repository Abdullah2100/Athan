package com.example.athan.ui.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.athan.R
import com.example.athan.ui.view.component.AthanNextTimeComponent
import com.example.athan.ui.view.component.AthanShape
import com.example.athan.ui.view.component.Sizer
import com.example.athan.util.General.toValidDayHour
import com.example.athan.viewModel.AthanViewModel
import com.example.athan.viewModel.ConnectivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    athanViewModel: AthanViewModel,
    connectivityModel: ConnectivityViewModel
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val fixedWidth = (screenWidth / 3)

    val state = rememberPullToRefreshState()
    val coroutine = rememberCoroutineScope()

    val dayAthens = athanViewModel.athanDay.collectAsState();
    val nextAthanTime = athanViewModel.currentAthanOb.collectAsState();
    val isNetworkAvailable = connectivityModel.isConnectToInternet.collectAsState()

    val isRefresh = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White, topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.prayer_times))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        })
    { innerPadding ->
        innerPadding.calculateTopPadding()
        innerPadding.calculateBottomPadding()


        PullToRefreshBox(
            isRefreshing = isRefresh.value,
            onRefresh = {
                if (isNetworkAvailable.value) {
                    coroutine.launch {

                        if (!isRefresh.value) {
                            isRefresh.value = true
                            delay(100)
                        }
                        athanViewModel.getAthanDates(isrefresh = isRefresh)

                    }
                }
            },
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding() + 1.dp)
                        .align(Alignment.TopCenter),
                    isRefreshing = isRefresh.value,
                    containerColor = Color.White,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = state
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(horizontal = 10.dp)
            ) {
                item {
                    AnimatedVisibility(
                        enter = fadeIn(tween(100)),
                        exit = fadeOut(tween(500)),
                        visible = nextAthanTime.value != null,
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,

                                )
                            {
                                AthanNextTimeComponent(
                                    fixedWidth,
                                    nextAthanTime.value?.hour.toString(),
                                    stringResource(R.string.hours)
                                )
                                Sizer(width = 5)
                                AthanNextTimeComponent(
                                    fixedWidth,
                                    nextAthanTime.value?.minute.toString(),
                                    stringResource(R.string.minutes)
                                )
                                Sizer(width = 5)
                                AthanNextTimeComponent(
                                    fixedWidth,
                                    nextAthanTime.value?.name.toString(),
                                    stringResource(R.string.salah)
                                )
                            }

                        }
                    )
                }

                item {
                    if (!dayAthens.value?.athanTimes.isNullOrEmpty()) {
                        Sizer(30)
                        AnimatedVisibility(
                            enter = fadeIn(tween(200)),
                            exit = fadeOut(tween(200)),
                            visible = dayAthens.value != null,
                            content = {
                                Column {
                                    dayAthens.value?.athanTimes?.forEachIndexed { index, value ->
                                        AthanShape(
                                            listOf(
                                                value.hour,
                                                value.minute
                                            ).toValidDayHour(context),
                                            stringResource(
                                                when (index) {
                                                    0 -> R.string.fajr
                                                    1 -> R.string.sunrise
                                                    2 -> R.string.dhuhr
                                                    3 -> R.string.asr
                                                    4 -> R.string.maghrib
                                                    else -> R.string.isha
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }


            }
        }
    }
}