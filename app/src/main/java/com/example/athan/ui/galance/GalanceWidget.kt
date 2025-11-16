package com.example.athan.ui.galance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.width
import com.example.athan.ui.view.component.AthanNextTimeComponent
import com.example.athan.ui.view.component.Sizer
import com.example.athan.util.UpdateAthanTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class GalanceWidget(
    var updateAthanTime: UpdateAthanTime
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Widget()
        }
    }

    @Composable
    fun Widget() {
        val nextAthanTime = updateAthanTime.nextAthanTime.collectAsState()
        val fixedWidth=50
        Column {
            Row(
                modifier = GlanceModifier
                    .width(150.toInt()),
                horizontalAlignment   = Alignment.CenterHorizontally ,

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
    }
}
