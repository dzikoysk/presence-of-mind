package net.dzikoysk.presenceofmind.pages.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.components.scaledSp
import net.dzikoysk.presenceofmind.model.presence.InMemoryPresenceRepository
import net.dzikoysk.presenceofmind.model.presence.PresenceRepository
import net.dzikoysk.presenceofmind.pages.Page

@Preview(showBackground = false)
@Composable
fun SettingsPreview() {
    Settings()
}

@Composable
fun Settings(
    presenceRepository: PresenceRepository = InMemoryPresenceRepository(),
    changePage: (Page) -> Unit = {}
) {
    val avatarUrl = remember { mutableStateOf(presenceRepository.getAvatarUrl()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .clickable {
                    presenceRepository.setAvatarUrl(avatarUrl.value)
                    changePage(Page.DASHBOARD)
                }
        ) {
            Icon(
                contentDescription = "Back to dashboard",
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Go back",
                fontSize = 15.scaledSp(),
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            Column {
                Text(
                    fontSize = 15.scaledSp(),
                    text = "Avatar URL",
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                TextField(
                    value = avatarUrl.value,
                    onValueChange = { avatarUrl.value = it  }
                )
            }
        }
    }
}