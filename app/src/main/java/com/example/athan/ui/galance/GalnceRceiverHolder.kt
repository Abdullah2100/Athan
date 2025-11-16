package com.example.athan.ui.galance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.athan.util.UpdateAthanTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalnceRceiverHolder: GlanceAppWidgetReceiver() {
    @Inject
    lateinit var updateAthanTime: UpdateAthanTime
    override val glanceAppWidget: GlanceAppWidget
        get() = GalanceWidget(updateAthanTime)
}