import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

var currentPlayer = 'X'

var isDone = false

var pointsX = 0
var pointsO = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun TicTacToeApp() {
    val board = remember { mutableStateListOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ') }

    var text by remember { mutableStateOf("Player $currentPlayer's turn") }

    var isVisible by remember { mutableStateOf(isDone) }

    Column {
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(9) { i ->
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(20))
                        .onClick {
                            if (board[i] == ' ' && !isDone) {
                                board[i] = currentPlayer
                                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                                text = checkWinningCondition(board, currentPlayer)
                                isVisible = isDone
                            } else if (!isDone) text = "This cell is occupied!\nChoose another one!"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${board[i]}", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, color = Color.DarkGray, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            AnimatedVisibility(visible = isVisible) {
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        for (i in board.indices) board[i] = ' '
                        isVisible = false
                        isDone = false
                        text = "Player $currentPlayer's turn"
                    },
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Reset")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Points: X → $pointsX, O → $pointsO")
        }
    }
}

fun checkWinningCondition(board: SnapshotStateList<Char>, currentPlayer: Char): String {
    for (i in 0..8 step 3) {
        // Check rows
        if (board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
            if (board[i] != ' ') {
                when (board[i]) {
                    'X' -> pointsX++
                    'O' -> pointsO++
                }
                isDone = true
                return "${board[i]} wins"
            }
        }
    }

    for (i in 0..2) {
        // Check columns
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6]) {
            if (board[i] != ' ') {
                when (board[i]) {
                    'X' -> pointsX++
                    'O' -> pointsO++
                }
                isDone = true
                return "${board[i]} wins"
            }
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8]) {
        if (board[0] != ' ') {
            when (board[0]) {
                'X' -> pointsX++
                'O' -> pointsO++
            }
            isDone = true
            return "${board[0]} wins"
        }
    }
    if (board[2] == board[4] && board[4] == board[6]) {
        if (board[2] != ' ') {
            when (board[2]) {
                'X' -> pointsX++
                'O' -> pointsO++
            }
            isDone = true
            return "${board[2]} wins"
        }
    }

    if (board.all { cell -> cell != ' ' }) {
        isDone = true
        return "Draw"
    }

    return "Player $currentPlayer's turn"
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition(alignment = Alignment.Center),
            size = DpSize(width = 240.dp, height = 410.dp)
        ),
        title = "Tic-Tac-Toe",
        resizable = false
    ) {
        TicTacToeApp()
    }
}
