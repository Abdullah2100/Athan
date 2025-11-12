package com.example.athan.ui.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun AthanNextTimeComponent(
    screenWidth:Int,
    title:String,
    description:String
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .width((screenWidth-10).dp)
            .height(70.dp)
            .background(
                Color(0xffF0F2F4),
                RoundedCornerShape(8.dp))
            ,

            contentAlignment = Alignment.Center

        ) {
            Text(title, style = TextStyle(fontWeight = FontWeight.Black))
        }
        Sizer(height = 4)
        Text(description)

    }
}