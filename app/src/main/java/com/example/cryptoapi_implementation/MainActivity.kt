package com.example.cryptoapi_implementation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cryptoapi_implementation.data.CoinDto
import com.example.cryptoapi_implementation.data.CoinsRepository
import com.example.cryptoapi_implementation.ui.theme.CryptoApiImplementationTheme
import com.example.cryptoapi_implementation.ui.theme.ui.CoinListScreen
import com.example.cryptoapi_implementation.ui.theme.ui.CoinRegistroScreen
import com.example.cryptoapi_implementation.ui.theme.ui.component.CoinItem
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import java.text.DecimalFormat
import javax.inject.Inject
import com.example.cryptoapi_implementation.util.Resource

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoApiImplementationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationHost()
                }
            }
        }
    }
}


@Composable
fun NavigationHost() {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screens.CoinListScreen.route
    ) {
        composable(Screens.CoinListScreen.route) {
            CoinListScreen(navHostController = navHostController)
        }
        composable(Screens.RegisterCoinScreen.route) {
            CoinRegistroScreen(navHostController = navHostController)
        }
    }
}

sealed class Screens(val route: String) {
    object CoinListScreen : Screens("CoinListScreen")
    object RegisterCoinScreen : Screens("CoinRegistroScreen")
}


