package com.example.athan.ui.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.athan.util.General.toCustomFil
import com.example.athan.viewModel.AthanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(athanViewModel: AthanViewModel) {
    val context = LocalContext.current
    val savedAThan = athanViewModel.athanSaved.collectAsState()


    val selectNewAthan = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let {
            athanViewModel.saveAthanToLocalAthan(it)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Settings")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.White

    ) { innerPadding ->
        innerPadding.calculateTopPadding()
        innerPadding.calculateBottomPadding()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 20.dp)
                .padding(horizontal = 15.dp),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            selectNewAthan.launch("audio/*")
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Athan Sound",
                        fontWeight = FontWeight.Bold,
                    )
                    if (savedAThan.value == null)
                        Text(
                            "Chose",
                            fontWeight = FontWeight.Light,
                            fontSize = 13.sp
                        )
                }
                when (savedAThan.value) {
                    null -> {
                        Text("Default")
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .background(Color.Green, RoundedCornerShape(20.dp))
                        )
                    }
                }
            }
        }

    }
}