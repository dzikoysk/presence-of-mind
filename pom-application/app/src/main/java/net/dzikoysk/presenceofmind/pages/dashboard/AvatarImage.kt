package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import net.dzikoysk.presenceofmind.R

@Composable
fun AvatarImage(
    openMenu: () -> Unit = {}
) {
    GlideImage(
        modifier = Modifier
            .width(32.dp)
            .height(32.dp)
            .clip(CircleShape)
            .clickable(onClick = { openMenu() }),
        imageModel = "https://avatars.githubusercontent.com/u/75123628?s=200&v=4",
        contentScale = ContentScale.Crop,
        circularReveal = CircularReveal(duration = 0),
        placeHolder = ImageVector.vectorResource(R.drawable.ic_baseline_check_circle_24),
        error = ImageVector.vectorResource(id = R.drawable.ic_baseline_account_circle_24)
    )
}