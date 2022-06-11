package net.dzikoysk.presenceofmind.components.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.task.OccurrencePolicy

@Composable
fun IntervalOption(
    selectedType: OccurrencePolicy,
    setSelectedType: (OccurrencePolicy) -> Unit,
    typeDropdownIsOpen: Boolean,
    setDropdownIsOpen: (Boolean) -> Unit
) {
    Box {
        var fieldWidth by remember { mutableStateOf(IntSize.Zero) }

        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            enabled = false,
            label = {
                Text(
                    text = "Select interval",
                    modifier = Modifier.background(MaterialTheme.colors.surface)
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                    contentDescription = "Open list of available intervals"
                )
            },
            modifier = Modifier
                .padding(vertical = 7.dp)
                .fillMaxWidth()
                .clickable { setDropdownIsOpen(!typeDropdownIsOpen) }
                .onGloballyPositioned { fieldWidth = it.size }
        )

        if (typeDropdownIsOpen) {
            DropdownMenu(
                expanded = typeDropdownIsOpen,
                onDismissRequest = { setDropdownIsOpen(false) },
                offset = DpOffset(x = 0.dp, y = (-8).dp),
                modifier = Modifier.width(LocalDensity.current.run { fieldWidth.width.toDp() })
            ) {
                OccurrencePolicy.values().forEach { occurrencePolicyType ->
                    DropdownMenuItem(
                        onClick = {
                            setSelectedType(occurrencePolicyType)
                            setDropdownIsOpen(false)
                        }
                    ) {
                        Text(
                            text = occurrencePolicyType.displayName,
                            fontWeight = if (selectedType == occurrencePolicyType) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}