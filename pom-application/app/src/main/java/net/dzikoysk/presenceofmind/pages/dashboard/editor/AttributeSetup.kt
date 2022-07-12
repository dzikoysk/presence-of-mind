package net.dzikoysk.presenceofmind.pages.dashboard.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.dzikoysk.presenceofmind.R
import net.dzikoysk.presenceofmind.data.attributes.Attribute

@Composable
fun <A : Attribute> AttributeSetup(
    attribute: A?,
    attributeDefaultInstance: A,
    attributeName: String,
    onEnable: () -> Unit,
    onDisable: () -> Unit,
    content: @Composable (A) -> Unit = {}
) {
    if (attribute == null) {
        Button(
            modifier = Modifier.padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.surface),
            shape = RoundedCornerShape(16.dp),
            onClick = { onEnable() }
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                //modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(attributeDefaultInstance.getDefaultAccentColor())
                )
                Text(
                    text = attributeName.replaceFirstChar { it.uppercase() },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    } else {
        Box(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(6.dp)
                    ),
                content = { content(attribute) }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(MaterialTheme.colors.surface)
                ) {
                    Text(
                        text = "Configure $attributeName",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                Box (
                    modifier = Modifier
                        .clickable { onDisable() }
                        .padding(horizontal = 10.dp)
                        .background(MaterialTheme.colors.surface)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_close_24),
                        contentDescription = "Delete attribute",
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}