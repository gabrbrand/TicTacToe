import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Desktop
import java.net.URI

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center),
            size = DpSize(width = 300.dp, height = 450.dp)
        ),
        title = "Tic-Tac-Toe",
        resizable = false,
    ) {
        TicTacToeApp()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun TicTacToeApp() {
    val board = remember { mutableStateListOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ') }

    var currentPlayer by remember { mutableStateOf('X') }

    var gameOver by remember { mutableStateOf(false) }

    var pointsX by remember { mutableStateOf(0) }
    var pointsO by remember { mutableStateOf(0) }
    var draw by remember { mutableStateOf(0) }

    Column {
        // Main toolbar
        Row(
            modifier = Modifier
                .background(color = Color(0xFF27282E))
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
                    onClick = {
                        for (i in board.indices) board[i] = ' '
                        currentPlayer = 'X'
                        gameOver = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "New game",
                        tint = Color.White
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
                        tint = Color.White
                    )
                }
            }
        }


        // Game Board
        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(9) { i ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(width = 1.dp, color = Color(0xFF27282E))
                        .clickable {
                            if (board[i] == ' ' && !gameOver) {
                                board[i] = currentPlayer

                                val winner = getWinner(board)
                                if (winner != ' ') {
                                    when (winner) {
                                        'X' -> pointsX++
                                        'O' -> pointsO++
                                    }
                                    gameOver = true
                                } else if (board.all { cell -> cell != ' ' }) {
                                    draw++
                                    gameOver = true
                                }

                                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${board[i]}", fontSize = 25.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        //Bottom bar
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.size(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Player 1 (X)",
                    fontWeight = if (currentPlayer == 'X' && !gameOver) FontWeight.ExtraBold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$pointsX",
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
                Text(text = "Draw")
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "$draw")
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
                    fontWeight = if (currentPlayer == 'O' && !gameOver) FontWeight.ExtraBold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$pointsO",
                    textDecoration = if (pointsO > pointsX) TextDecoration.Underline else TextDecoration.None
                )
            }
        }
    }
}

fun getWinner(board: SnapshotStateList<Char>): Char {
    for (i in 0..8 step 3) {
        // Check rows
        if (board[i] == board[i + 1] && board[i + 1] == board[i + 2]) {
            if (board[i] != ' ') {
                return board[i]
            }
        }
    }

    for (i in 0..2) {
        // Check columns
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6]) {
            if (board[i] != ' ') {
                return board[i]
            }
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8]) {
        if (board[0] != ' ') {
            return board[0]
        }
    }
    if (board[2] == board[4] && board[4] == board[6]) {
        if (board[2] != ' ') {
            return board[2]
        }
    }

    return ' '
}