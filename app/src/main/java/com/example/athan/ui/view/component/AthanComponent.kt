package com.example.athan.ui.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun AthanShape(time:String,name:String)
{
    Row(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .height(30.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceBetween,

        )
    {
        Text(text =time, fontWeight = FontWeight.Normal)
        Text(text =name, fontWeight = FontWeight.SemiBold)
    }
}

