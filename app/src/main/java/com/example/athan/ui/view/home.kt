package com.example.athan.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.athan.ui.view.component.AthanNextTimeComponent
import com.example.athan.ui.view.component.AthanShape
import com.example.athan.ui.view.component.Sizer
import com.example.athan.util.General.toValidDayHour
import com.example.athan.viewModel.AthanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(athanViewModel: AthanViewModel) {

    val dayAthans = athanViewModel.athanDay.collectAsState();
    val nextAthanTime = athanViewModel.currentAthanOb.collectAsState();

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val fixedWidth = (screenWidth / 3)
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text("مواقيت الصلاة")
            }
        )
    })
    { innerPadding ->
        innerPadding.calculateTopPadding()
        innerPadding.calculateBottomPadding()


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = 10.dp)
        ) {
            item {
                AnimatedVisibility(
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
                                "Hours"
                            )
                            Sizer(width = 5)
                            AthanNextTimeComponent(
                                fixedWidth,
                                nextAthanTime.value?.minute.toString(),
                                "Minutes"
                            )
                            Sizer(width = 5)
                            AthanNextTimeComponent(
                                fixedWidth,
                                nextAthanTime.value?.name.toString(),
                                "Salah"
                            )
                        }

                    }
                )
            }

            item {
                if (!dayAthans.value?.athanTimes.isNullOrEmpty()) {
                    Sizer(25)
                    AnimatedVisibility(
                        visible = dayAthans.value != null,
                        content = {
                            Column {
                                dayAthans.value?.athanTimes?.forEach { value ->
                                    AthanShape(
                                        listOf(value.hour, value.minute).toValidDayHour(),
                                        value.name
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