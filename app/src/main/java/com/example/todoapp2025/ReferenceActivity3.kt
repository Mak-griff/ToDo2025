package com.example.todoapp2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class ReferenceActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GuessingGameScreen(
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
                    onNavigateToTic = {
                        val intent = Intent(this, ReferenceActivity4::class.java)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessingGameScreen(
    onNavigateToMain: () -> Unit, onNavigateToCat: () -> Unit, onNavigateToLight: () -> Unit, onNavigateToTic: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var range by remember { mutableStateOf(100) }
    var guessInput by remember { mutableStateOf("") }
    var targetNumber by remember { mutableStateOf((1..range).random()) }
    var message by remember { mutableStateOf("Make a guess!") }
    val guesses = remember { mutableStateListOf<Int>() }
    var expanded by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Navigate",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Main") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToMain()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Cat") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToCat()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Light") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToLight()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Guessing Game") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Tic Tac Toe") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToTic()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Guessing Game") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0))
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Select Range", fontSize = 20.sp)
                Box {
                    Button(onClick = { expanded = true }) {
                        Text("1 - $range")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("1 - 10") }, onClick = {
                            range = 10
                            targetNumber = (1..range).random()
                            guesses.clear()
                            message = "New range selected!"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("1 - 50") }, onClick = {
                            range = 50
                            targetNumber = (1..range).random()
                            guesses.clear()
                            message = "New range selected!"
                            expanded = false
                        })
                        DropdownMenuItem(text = { Text("1 - 100") }, onClick = {
                            range = 100
                            targetNumber = (1..range).random()
                            guesses.clear()
                            message = "New range selected!"
                            expanded = false
                        })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = guessInput,
                    onValueChange = { guessInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Enter your guess") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    val guess = guessInput.toIntOrNull()

                    if (guess == null) {
                        message = "Please enter a valid number!"
                    } else {
                        guesses.add(guess)
                        message = when {
                            guess < targetNumber -> "Too low"
                            guess > targetNumber -> "Too high"
                            else -> "Correct!"
                        }
                    }

                    guessInput = ""
                }) {
                    Text("Guess")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = message,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Previous guesses:", fontSize = 18.sp)
                guesses.forEach { Text("- $it", fontSize = 18.sp) }

                Spacer(modifier = Modifier.height(30.dp))

                Button(onClick = {
                    targetNumber = (1..range).random()
                    guesses.clear()
                    message = "Game reset! Make a guess."
                }) {
                    Text("Restart Game")
                }
            }
        }
    }
}