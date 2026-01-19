package com.example.cryptooffline.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptooffline.data.local.CoinEntity
import com.example.cryptooffline.domain.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarketUiState(
    val isLoading: Boolean = true,
    val coins: List<CoinEntity> = emptyList(),
    val error: String? = null,
    val selectedCurrency: String = "usd"
)

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val coinRepository: CoinRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    init {

        // Observar la moneda seleccionada y actualizar el estado de la UI
        coinRepository.getCurrencyFlow()
            .onEach { currency ->
                _uiState.value = _uiState.value.copy(selectedCurrency = currency)
            }
            .launchIn(viewModelScope)

        // Observar la lista de monedas de la BD
        coinRepository.getAllCoins()
            .onEach { coinList ->
                _uiState.value = _uiState.value.copy(coins = coinList, isLoading = false)
            }
            .catch { error ->
                _uiState.value =
                    _uiState.value.copy(error = error.localizedMessage, isLoading = false)
            }
            .launchIn(viewModelScope)
        refreshData()
    }

//Refresco inical
    fun refreshData() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                coinRepository.refreshMarketList()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.localizedMessage, isLoading = false)
            }
        }
    }

    fun onFavoriteClicked(coinId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            coinRepository.updateFavoriteStatus(coinId, !isFavorite)
        }
    }

    // Después de cambiar la moneda, los datos se refrescarán automáticamente
    fun onCurrencyChange(newCurrency: String) {
        viewModelScope.launch {
            coinRepository.setCurrency(newCurrency)
            refreshData()
        }
    }
}




