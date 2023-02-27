package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import ui.theme.AppTheme

@Composable
@Preview
fun TicTacToeApp() {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    var isDark by remember { mutableStateOf(isSystemInDarkTheme) }

    val board = remember { mutableStateListOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ') }

    var currentPlayer by remember { mutableStateOf('X') }

    var winnerCoordinates by remember { mutableStateOf(emptyList<Int>()) }
    var winnerCharacter by remember { mutableStateOf(' ') }

    var pointsX by remember { mutableStateOf(0) }
    var pointsO by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }

    var isGameOver by remember { mutableStateOf(false) }

    val reset = {
        for (i in board.indices) board[i] = ' '
        currentPlayer = 'X'
        isGameOver = false
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
                isAppThemeDark = isDark,
                onThemeSwitcherClick = { isDark = !isDark }
            )

            GameBoard(
                board = board,
                onCellClick = { i ->
                    if (board[i] == ' ' && !isGameOver) {
                        board[i] = currentPlayer

                        val winner = getWinner(board)
                        winnerCoordinates = winner.coordinates
                        winnerCharacter = winner.character
                        if (winnerCharacter != ' ') {
                            when (winnerCharacter) {
                                'X' -> pointsX++
                                'O' -> pointsO++
                            }
                            isGameOver = true
                        } else if (board.all { cell -> cell != ' ' }) {
                            draws++
                            isGameOver = true
                        }

                        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                    } else if (isGameOver) {
                        reset()
                    }
                },
                winnerCoordinates = winnerCoordinates,
                isGameOver = isGameOver
            )

            BottomBar(
                currentPlayer = currentPlayer,
                winnerCharacter = winnerCharacter,
                isGameOver = isGameOver,
                pointsX = pointsX,
                pointsO = pointsO,
                draws = draws
            )
        }
    }
}

data class Winner(
    val character: Char = ' ',
    val coordinates: List<Int> = emptyList()
)

fun getWinner(board: SnapshotStateList<Char>): Winner {
    for (i in 0..8 step 3) {
        // Check rows
        if (board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
            if (board[i] != ' ') {
                return Winner(
                    character = board[i],
                    coordinates = listOf(i, i + 1, i + 2)
                )
            }
        }
    }

    for (i in 0..2) {
        // Check columns
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6]) {
            if (board[i] != ' ') {
                return Winner(
                    character = board[i],
                    coordinates = listOf(i, i + 3, i + 6)
                )
            }
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8]) {
        if (board[0] != ' ') {
            return Winner(
                character = board[0],
                coordinates = listOf(0, 4, 8)
            )
        }
    }
    if (board[2] == board[4] && board[4] == board[6]) {
        if (board[2] != ' ') {
            return Winner(
                character = board[2],
                coordinates = listOf(2, 4, 6)
            )
        }
    }

    return Winner()
}