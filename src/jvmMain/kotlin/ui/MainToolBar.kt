package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.awt.Desktop
import java.net.URI

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainToolBar(
    onNewGameButtonClick: () -> Unit,
    appThemeIsDark: Boolean,
    onThemeSwitcherClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .height(40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TooltipArea(
            tooltip = {
                Surface(
                    modifier = Modifier.shadow(4.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "New game",
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        ) {
            IconButton(
                onClick = onNewGameButtonClick
            ) {
                Icon(
                    painter = painterResource("icons/new_game.svg"),
                    contentDescription = "New game",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }

        Row {
            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Open GitHub link in browser",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            ) {
                IconButton(
                    onClick = {
                        Desktop.getDesktop().browse(URI.create("https://github.com/gabrbrand/TicTacToe"))
                    }
                ) {
                    Icon(
                        painter = painterResource("icons/github.svg"),
                        contentDescription = "Open GitHub link in browser",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            TooltipArea(
                tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Switch to ${if (appThemeIsDark) "light" else "dark"} mode",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            ) {
                IconButton(
                    onClick = onThemeSwitcherClick
                ) {
                    Icon(
                        painter = painterResource(resourcePath = if (appThemeIsDark) "icons/light_mode.svg" else "icons/dark_mode.svg"),
                        contentDescription = "Switch to ${if (appThemeIsDark) "light" else "dark"} mode",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}