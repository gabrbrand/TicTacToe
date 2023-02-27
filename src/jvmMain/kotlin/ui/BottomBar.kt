package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    currentPlayer: Char,
    winnerCharacter: Char,
    isGameOver: Boolean,
    pointsX: Int,
    pointsO: Int,
    draws: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.size(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Player 1 (X)",
                color = if (winnerCharacter == 'X' && isGameOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                fontWeight = if (currentPlayer == 'X' && !isGameOver || winnerCharacter == 'X' && isGameOver) FontWeight.ExtraBold else FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = pointsX.toString(),
                color = MaterialTheme.colors.onBackground,
                textDecoration = if (pointsX > pointsO) TextDecoration.Underline else TextDecoration.None
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .background(color = Color.LightGray)
                .width(2.dp)
        )

        Column(
            modifier = Modifier.size(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Draw",
                color = if (winnerCharacter == ' ' && isGameOver) MaterialTheme.colors.error else MaterialTheme.colors.onBackground,
                fontWeight = if (winnerCharacter == ' ' && isGameOver) FontWeight.ExtraBold else FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = draws.toString(),
                color = MaterialTheme.colors.onBackground
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .background(color = Color.LightGray)
                .width(2.dp)
        )

        Column(
            modifier = Modifier.size(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Player 2 (O)",
                color = if (winnerCharacter == 'O' && isGameOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                fontWeight = if (currentPlayer == 'O' && !isGameOver || winnerCharacter == 'O' && isGameOver) FontWeight.ExtraBold else FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = pointsO.toString(),
                color = MaterialTheme.colors.onBackground,
                textDecoration = if (pointsO > pointsX) TextDecoration.Underline else TextDecoration.None
            )
        }
    }
}