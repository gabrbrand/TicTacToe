package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppTheme

@Composable
@Preview
fun TicTacToeApp() {
    var isDark by remember { mutableStateOf(false) }

    val board = remember { mutableStateListOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ') }

    var currentPlayer by remember { mutableStateOf('X') }

    var winnerCoordinates by remember { mutableStateOf(listOf(-1, -1, -1)) }
    var winnerCharacter by remember { mutableStateOf(' ') }

    var pointsX by remember { mutableStateOf(0) }
    var pointsO by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }

    var gameIsOver by remember { mutableStateOf(false) }

    val reset = {
        for (i in board.indices) board[i] = ' '
        currentPlayer = 'X'
        gameIsOver = false
    }

    AppTheme(
        darkTheme = isDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            MainToolBar(
                onNewGameButtonClick = reset,
                appThemeIsDark = isDark,
                onThemeSwitcherClick = { isDark = !isDark }
            )

            // Game Board
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(count = 9) { i ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(width = 1.dp, color = MaterialTheme.colors.primary)
                            .clickable {
                                if (board[i] == ' ' && !gameIsOver) {
                                    board[i] = currentPlayer

                                    winnerCoordinates = getWinnerCoordinates(board)
                                    winnerCharacter = if (winnerCoordinates.first() != -1) board[winnerCoordinates.first()] else ' '
                                    if (winnerCharacter != ' ') {
                                        when (winnerCharacter) {
                                            'X' -> pointsX++
                                            'O' -> pointsO++
                                        }
                                        gameIsOver = true
                                    } else if (board.all { cell -> cell != ' ' }) {
                                        draws++
                                        gameIsOver = true
                                    }

                                    currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                                } else if (gameIsOver) {
                                    reset()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[i].toString(),
                            color = if (i in winnerCoordinates && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            //Bottom bar
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
                        color = if (winnerCharacter == 'X' && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                        fontWeight = if (currentPlayer == 'X' && !gameIsOver || winnerCharacter == 'X' && gameIsOver) FontWeight.ExtraBold else FontWeight.Normal
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
                        color = if (winnerCharacter == ' ' && gameIsOver) MaterialTheme.colors.error else MaterialTheme.colors.onBackground,
                        fontWeight = if (winnerCharacter == ' ' && gameIsOver) FontWeight.ExtraBold else FontWeight.Normal
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
                        color = if (winnerCharacter == 'O' && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                        fontWeight = if (currentPlayer == 'O' && !gameIsOver || winnerCharacter == 'O' && gameIsOver) FontWeight.ExtraBold else FontWeight.Normal
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
    }
}

fun getWinnerCoordinates(board: SnapshotStateList<Char>): List<Int> {
    for (i in 0..8 step 3) {
        // Check rows
        if (board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
            if (board[i] != ' ') {
                return listOf(i, i + 1, i + 2)
            }
        }
    }

    for (i in 0..2) {
        // Check columns
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6]) {
            if (board[i] != ' ') {
                return listOf(i, i + 3, i + 6)
            }
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8]) {
        if (board[0] != ' ') {
            return listOf(0, 4, 8)
        }
    }
    if (board[2] == board[4] && board[4] == board[6]) {
        if (board[2] != ' ') {
            return listOf(2, 4, 6)
        }
    }

    return listOf(-1, -1, -1)
}