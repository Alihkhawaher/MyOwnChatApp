package com.example.myownchat.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myownchat.data.User
import com.example.myownchat.R
import com.example.myownchat.data.SETTINGS

@Composable
fun SettingsScreen(user: User?){

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 70.dp
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.decoy_person_image),
            contentDescription = "Person image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(256.dp)
                .clip(CircleShape)
                .border(5.dp, Color.LightGray, CircleShape)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = user!!.login,
            fontSize = (adaptiveFontSize(screenWidthDp = screenWidthDp)+10).sp
        )
        Text(
            text = user!!.email,
            fontSize = (adaptiveFontSize(screenWidthDp = screenWidthDp)+5).sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
        ) {
            itemsIndexed(SETTINGS){ index, setting ->
                SettingsButton(
                    buttonIconName = setting.iconName,
                    buttonText = setting.settingsName,
                    onClick = {
                        Log.d("Settings_button", "${setting.settingsName} is clicked")
                    },
                    index = index
                )
            }
        }
    }
}

@Composable
fun SettingsButton(
    buttonIconName: String,
    buttonText: String,
    onClick: () -> Unit,
    index: Int){

    val backgroundColor: Color = if (index % 2 == 0) Color.LightGray else Color.White
    val insidesColor: Color = if (index % 2 == 0) Color.White else Color.Black

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(PaddingValues(horizontal = 15.dp, vertical = 5.dp))
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = loadIconFromDrawableWithString(buttonIconName),
            contentDescription = null,
            tint = insidesColor,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = buttonText, fontSize = adaptiveFontSize(screenWidthDp = screenWidthDp).sp, color = insidesColor)
    }
}

@Composable
fun loadIconFromDrawableWithString(iconName: String): Painter {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(iconName, "drawable", context.packageName)
    return painterResource(id = resourceId)
}

@Composable
fun adaptiveFontSize(screenWidthDp: Int): Float {
    return when {
        screenWidthDp < 360 -> 12.sp.value
        screenWidthDp < 600 -> 16.sp.value
        else -> 20.sp.value
    }
}