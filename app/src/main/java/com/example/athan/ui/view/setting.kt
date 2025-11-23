package com.example.athan.ui.view

import android.app.Dialog
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.athan.R
import com.example.athan.util.General.toCustomFil
import com.example.athan.viewModel.AthanViewModel
import com.example.athan.viewModel.LocaleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import  androidx.compose.ui.window.*
import com.example.athan.ui.view.component.Sizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(athanViewModel: AthanViewModel, localeViewModel: LocaleViewModel) {
    val context = LocalContext.current
    val savedAThan = athanViewModel.athanSaved.collectAsState()
    val currentLocale = localeViewModel.savedLocale.collectAsState()

    val coroutine = rememberCoroutineScope()
    val isExpandLanguage = remember { mutableStateOf<Boolean>(false) }
    val isShowDialog = remember { mutableStateOf<Boolean>(false) }

    val selectNewAthan = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let {
            val file = it.toCustomFil(context)?.absoluteFile
            athanViewModel.saveAthanToLocalAthan(file?.toUri())
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
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

            )
        {
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
            )
            {
                Column {
                    Text(
                        stringResource(R.string.athan_sound),
                        fontWeight = FontWeight.Bold,
                    )
                    if (savedAThan.value == null)
                        Text(
                            stringResource(R.string.chose),
                            fontWeight = FontWeight.Light,
                            fontSize = 13.sp
                        )
                }
                when (savedAThan.value) {
                    null -> {
                        Text(stringResource(R.string._default))
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

            Sizer(20)
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable(onClick = {
                        isExpandLanguage.value = true
                    }),
                Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Language",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        if (currentLocale.value?.name == "ar") "العربية" else "English",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center

                    )
                }
                Icon(Icons.Default.Language, "",)
            }
            DropdownMenu(
                containerColor = Color.White,
                expanded = isExpandLanguage.value,
                onDismissRequest = { isExpandLanguage.value = false })
            {
                listOf<String>(
                    "العربية",
                    "English"
                ).forEach { lang ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                lang,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center

                            )
                        },
                        onClick = {
                            coroutine.launch {
                                isExpandLanguage.value = false
                                isShowDialog.value = true
                                delay(500)
                                val locale = if (lang == "العربية") "ar" else "en"
                                localeViewModel.whenLanguageUpdateDo(locale)
                                isShowDialog.value = false
                            }
                        }
                    )
                }
            }
        }

        if (isShowDialog.value) {
            Dialog(
                onDismissRequest = {}
            ) {
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .background(
                            Color.White, RoundedCornerShape(15.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.Green, modifier = Modifier.size(40.dp)
                    )
                }
            }

        }

    }
}