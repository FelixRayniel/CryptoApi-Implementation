package com.example.cryptoapi_implementation.ui.theme.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composable.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.cryptoapi_implementation.ui.theme.ui.component.CoinItem

@Composable
fun CoinListScreen(
    viewModel: CoinViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val state = viewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },

                actions = {
                    IconButton(onClick = { navHostController.navigate("CoinRegistroScreen")

                    }) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "AGREGAR",
                            modifier = Modifier.size(30.dp)
                        )

                    }
                }
            )
        }

    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.coins) { coin ->
                    CoinItem(coin = coin, {})
                }
            }

            if (state.isLoading)
                CircularProgressIndicator()
        }
    }
}