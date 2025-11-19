package com.example.todoapp2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class ReferenceActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {

                TicTacToeScreen(
                    onNavigateToMain = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onNavigateToCat = {
                        startActivity(Intent(this, ReferenceActivity1::class.java))
                        finish()
                    },
                    onNavigateToLight = {
                        startActivity(Intent(this, ReferenceActivity2::class.java))
                        finish()
                    },
                    onNavigateToGuess = {
                        startActivity(Intent(this, ReferenceActivity3::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToCat: () -> Unit,
    onNavigateToLight: () -> Unit,
    onNavigateToGuess: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var board by remember { mutableStateOf(List(9) { "" }) }
    var playerTurn by remember { mutableStateOf(true) }
    var gameMessage by remember { mutableStateOf("Your Turn (X)") }
    var gameOver by remember { mutableStateOf(false) }

    // Determine win
    fun checkWin(symbol: String): Boolean {
        val wins = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6),
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8),
            listOf(2, 4, 6)
        )
        return wins.any { (a, b, c) -> board[a] == symbol && board[b] == symbol && board[c] == symbol }
    }

    // CPU chooses random valid move
    fun cpuMove() {
        if (gameOver) return

        val empty = board.mapIndexed { i, v -> if (v == "") i else null }
            .filterNotNull()

        // No more spots available
        if (empty.isEmpty()) {
           gameMessage = "Draw!"
            return
        }

        val choice = empty.random()
        board = board.toMutableList().also { it[choice] = "O" }

        if (checkWin("O")) {
            gameMessage = "Computer Wins!"
            gameOver = true
        } else {
            playerTurn = true
            gameMessage = "Your Turn (X)"
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Text(
                    text = "Navigate",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                NavigationDrawerItem(
                    label = { Text("Main") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToMain()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Cat") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToCat()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Light") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToLight()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Guess Game") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToGuess()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Tic Tac Toe") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Tic Tac Toe") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = gameMessage,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )

                // Game board
                Column {
                    for (row in 0 until 3) {
                        Row {
                            for (col in 0 until 3) {
                                val index = row * 3 + col

                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .border(2.dp, Color.Black)
                                        .background(Color(0xFFF2F2F2))
                                        .clickable(enabled = !gameOver && board[index] == "") {
                                            if (playerTurn) {
                                                board = board.toMutableList().also {
                                                    it[index] = "X"
                                                }

                                                if (checkWin("X")) {
                                                    gameMessage = "You Win!"
                                                    gameOver = true
                                                } else {
                                                    playerTurn = false
                                                    gameMessage = "Computer's Turn..."
                                                    cpuMove()
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = board[index],
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        board = List(9) { "" }
                        playerTurn = true
                        gameOver = false
                        gameMessage = "Your Turn (X)"
                    }
                ) {
                    Text("Restart Game")
                }
            }
        }
    }
}
