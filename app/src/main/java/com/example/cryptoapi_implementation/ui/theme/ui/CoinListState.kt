package com.example.cryptoapi_implementation.ui.theme.ui

import com.example.cryptoapi_implementation.data.CoinDto

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinDto> = emptyList(),
    val error: String = ""
)