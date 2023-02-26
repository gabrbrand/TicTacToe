package ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameBoard(
    board: SnapshotStateList<Char>,
    onCellClick: (Int) -> Unit,
    winnerCoordinates: List<Int>,
    gameIsOver: Boolean
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(count = 9) { i ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(width = 1.dp, color = MaterialTheme.colors.primary)
                    .clickable { onCellClick(i) },
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
}