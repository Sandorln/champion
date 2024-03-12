package com.sandorln.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sandorln.design.theme.Colors

enum class CircleIconType {
    ITEM,
    CHAMPION
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BaseCircleIconImage(
    modifier: Modifier = Modifier,
    circleIconType: CircleIconType = CircleIconType.CHAMPION,
    versionName: String = "",
    id: String = ""
) {
    val url = "https://ddragon.leagueoflegends.com/cdn/${versionName}/img/${circleIconType.name.lowercase()}/${id}.png"

    GlideImage(
        modifier = modifier
            .background(Colors.Blue06, CircleShape)
            .clip(CircleShape)
            .border(1.dp, Colors.Gold05, CircleShape),
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}