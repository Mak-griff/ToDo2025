package com.example.todoapp2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class ReferenceActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LightScreen(
                    onNavigateToMain = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },

                    onNavigateToCat = {
                        val intent = Intent(this, ReferenceActivity1::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onNavigateToGuess = {
                        val intent = Intent(this, ReferenceActivity3::class.java)
                        startActivity(intent)
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
fun LightScreen(onNavigateToMain: () -> Unit, onNavigateToCat: () -> Unit, onNavigateToGuess: () -> Unit, onNavigateToTic: () -> Unit) {
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // Track on/off state
    var isLightOn by remember { mutableStateOf(true) }

    // Backgrounds
    val backgroundColor = if (isLightOn) Color(0xFFFFF8E1) else Color(0xFF1A1A1A)

    // Text color for readability on bright/dark backgrounds
    val textColor = if (isLightOn) Color(0xFF3A3A3A) else Color(0xFFE8E8E8)

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
                    selected = true,
                    onClick = {
                        // Already on Light screen, just close the drawer
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Guessing Game") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToGuess()
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
                    title = { Text("Light Screen") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { padding ->

            // ----- Background wrapper -----
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ----- Glow halo when light is ON -----
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow halo BEHIND the image
                        if (isLightOn) {
                            Box(
                                modifier = Modifier
                                    .size(350.dp)   // size of glow
                                    .graphicsLayer { alpha = 0.8f }
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFFFFF4C2),
                                                Color.Transparent
                                            ),
                                            radius = 350f
                                        ),
                                        shape = CircleShape
                                    )
                            )
                        }

                        // Light bulb image ON TOP of glow
                        Image(
                            painter = painterResource(
                                id = if (isLightOn) R.drawable.on else R.drawable.off
                            ),
                            contentDescription = "Light switch",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable { isLightOn = !isLightOn },
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ----- Text stays readable in both modes -----
                    Text(
                        text = "Try switching the light on and off",
                        fontSize = 18.sp,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}