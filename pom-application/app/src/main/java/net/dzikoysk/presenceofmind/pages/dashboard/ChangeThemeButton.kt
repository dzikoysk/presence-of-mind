package net.dzikoysk.presenceofmind.pages.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.theme.ThemeRepository

@Composable
fun ChangeThemeButton(
    themeRepository: ThemeRepository,
    restartActivity: () -> Unit
) {
    Box(Modifier.padding(horizontal = 16.dp)) {
        Icon(
            contentDescription = "Change color theme",
            painter = painterResource(
                id = when (themeRepository.isLightMode()) {
                    true -> R.drawable.ic_baseline_dark_mode_24
                    false -> R.drawable.ic_baseline_light_mode_24
                }
            ),
            modifier = Modifier
                .clickable {
                    themeRepository.setColorMode(
                        isLightMode = themeRepository
                            .isLightMode()
                            .not()
                    )
                    restartActivity()
                }
                .size(25.dp)
                .padding(top = 1.dp)
        )
    }
}