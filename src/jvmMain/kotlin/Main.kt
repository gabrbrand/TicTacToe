import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import ui.theme.AppTheme
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
    var isDark by remember { mutableStateOf(false) }

    val board = remember { mutableStateListOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ') }

    var currentPlayer by remember { mutableStateOf('X') }

    var coordinates by remember { mutableStateOf(listOf(-1, -1, -1)) }
    var winner by remember { mutableStateOf(' ') }

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
            // Main toolbar
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
                        onClick = {
                            reset()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
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
                                    text = "Switch to ${if (isDark) "light" else "dark"} mode",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                isDark = !isDark
                            }
                        ) {
                            Icon(
                                painter = painterResource(resourcePath = if (isDark) "icons/lightTheme.svg" else "icons/darkTheme.svg"),
                                contentDescription = "Switch to ${if (isDark) "light" else "dark"} mode",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }


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

                                    coordinates = getWinnerCoordinates(board)
                                    winner = if (coordinates.first() != -1) board[coordinates.first()] else ' '
                                    if (winner != ' ') {
                                        when (winner) {
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
                            color = if (i in coordinates && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold
                        )
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
                        color = if (winner == 'X' && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                        fontWeight = if (currentPlayer == 'X' && !gameIsOver || winner == 'X' && gameIsOver) FontWeight.ExtraBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
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
                        color = if (winner == ' ' && gameIsOver) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = draws.toString(), color = MaterialTheme.colors.onBackground)
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
                        color = if (winner == 'O' && gameIsOver) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                        fontWeight = if (currentPlayer == 'O' && !gameIsOver || winner == 'O' && gameIsOver) FontWeight.ExtraBold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(6.dp))
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