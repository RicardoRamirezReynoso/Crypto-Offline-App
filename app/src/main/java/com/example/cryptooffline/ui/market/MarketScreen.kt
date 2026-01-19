package com.example.cryptooffline.ui.market

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(
    viewModel: MarketViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Estado para controlar si el menú desplegable está abierto o cerrado
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criptomonedas") },
                actions = {
                    // Botón de tres puntos para abrir el menú
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                    }
                    // Menú desplegable
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cambiar a USD") },
                            onClick = {
                                viewModel.onCurrencyChange("usd")
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cambiar a MXN") },
                            onClick = {
                                viewModel.onCurrencyChange("mxn")
                                menuExpanded = false
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Lógica de la UI basada en el estado
            if (uiState.isLoading && uiState.coins.isEmpty()) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = uiState.coins,
                        key = { coin -> coin.id } // Se usa el ID de la moneda como clave única
                    ) { coin ->
                        //Conecta con la logica para agregar o quitar favoritos
                        CoinRow(
                            coin = coin,
                            currency = uiState.selectedCurrency,
                            onFavoriteClick = { coinId, isFavorite ->
                                viewModel.onFavoriteClicked(coinId, isFavorite)
                            }
                        )
                    }
                }
            }
        }
    }
}