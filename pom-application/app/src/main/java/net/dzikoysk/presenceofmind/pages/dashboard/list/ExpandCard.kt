package net.dzikoysk.presenceofmind.pages.dashboard.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R

@Preview(showBackground = true)
@Composable
fun ExpandCardPreview() {
    val isOpen = remember { mutableStateOf(true) }

    ExpandCard(
        isOpen = isOpen.value,
        setIsOpen = { isOpen.value = it }
    )
}

@Composable
fun ExpandCard(
    isOpen: Boolean,
    setIsOpen: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 5.dp)
            .clickable { setIsOpen(!isOpen) }
    ) {
        Icon(
            painter =
            if (isOpen)
                painterResource(id = R.drawable.ic_baseline_arrow_drop_up_24)
            else
                painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
            contentDescription = "Open repetitive task dashboard"
        )
    }
}