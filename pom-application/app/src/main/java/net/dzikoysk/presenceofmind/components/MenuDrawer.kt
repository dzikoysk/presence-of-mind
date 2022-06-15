package net.dzikoysk.presenceofmind.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.skydoves.landscapist.rememberDrawablePainter
import net.dzikoysk.presenceofmind.BuildConfig
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.shared.scaledSp

@Preview(showBackground = true)
@Composable
fun MenuDrawerPreview() {
    MenuDrawer(
        closeMenu = {}
    )
}

@Composable
fun MenuDrawer(
    closeMenu: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val logoPainter = rememberDrawablePainter(drawable = ContextCompat.getDrawable(LocalContext.current, R.mipmap.ic_launcher))
    val urlHandler = LocalUriHandler.current

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
       Column(
           modifier = Modifier
               .fillMaxHeight()
               .fillMaxWidth(0.7f)
               .background(MaterialTheme.colors.background)
               .padding(24.dp)
       ) {
           Row(verticalAlignment = Alignment.CenterVertically) {
               Image(
                   contentDescription = "Logo",
                   painter = logoPainter,
                   modifier = Modifier
                       .size(42.dp)
                       .clip(CircleShape)
               )
               Text(
                   text = "Presence of Mind",
                   fontWeight = FontWeight.Bold,
                   fontSize = 15.scaledSp(),
                   modifier = Modifier.padding(horizontal = 16.dp)
               )
           }
           Column(Modifier.padding(vertical = 16.dp)) {
               Text(
                   text = "Sources",
                   fontWeight = FontWeight.SemiBold,
                   fontSize = 15.scaledSp(),
                   modifier = Modifier
                       .clickable { urlHandler.openUri("https://github.com/dzikoysk/presence-of-mind") }
                       .padding(vertical = 8.dp)
               )
               Text(
                   text = "Report issue",
                   fontWeight = FontWeight.SemiBold,
                   fontSize = 15.scaledSp(),
                   modifier = Modifier
                       .clickable { urlHandler.openUri("https://github.com/dzikoysk/presence-of-mind/issues") }
                       .padding(vertical = 8.dp)
               )
           }
           Column(
               modifier = Modifier
                   .fillMaxHeight()
                   .fillMaxWidth(),
               verticalArrangement = Arrangement.Bottom,
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
               Text(
                   text = "Version ${BuildConfig.VERSION_NAME} ${BuildConfig.BUILD_TYPE} (${BuildConfig.VERSION_CODE})",
                   fontSize = 9.sp,
                   fontStyle = FontStyle.Italic,
                   color = MaterialTheme.colors.onSurface // Color(0x99ffffff)
               )
           }
       }
       Box(
           modifier = Modifier
               .fillMaxSize()
               .background(Color(0x11222222))
               .clickable(
                   interactionSource = interactionSource,
                   indication = null,
                   onClick = { closeMenu() }
               )
       )
    }
}