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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cryptoapi_implementation.ui.theme.CryptoApiImplementationTheme
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
                    CoinListScreen()
                }
            }
        }
    }
}

@Composable
fun CoinListScreen(
    viewModel: CoinViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items( state.coins){ coin ->
                CoinItem(coin = coin, {})
            }
        }

        if (state.isLoading)
            CircularProgressIndicator()

    }

}

@Composable
fun CoinItem(
    coin:CoinDto,
    onClick : (CoinDto) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp).fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .clickable { onClick(coin) }.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row{
                AsyncImage(modifier = Modifier.size(55.dp),
                    model = ImageRequest.Builder(LocalContext.current).data(coin.imageUrl)
                        .crossfade(true).build(),
                    contentDescription = coin.descripcion,
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(text = coin.descripcion, fontWeight = FontWeight.Bold)
            }

            val decimal = DecimalFormat("#,###.######")

            Text(
                text = "$" + decimal.format(coin.valor.toDouble()), color = Color.Blue, fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun CoinScreen(viewModel: CoinViewModel = hiltViewModel()) {
    //val coin = viewModel.coin.value

    Column(modifier = Modifier.fillMaxSize()) {
        /* Text(text = coin.name)
         Text(text = coin.symbol)*/
    }

}

data class CoinDto(
    val monedaId: Int = 0,
    val descripcion: String = "",
    val valor: Double = 0.00,
    val imageUrl: String = ""
)

//RUTA: data/remote
interface CoinApi {
    @GET("/Coins")
    suspend fun getCoins(): List<CoinDto>
}


class CoinsRepository @Inject constructor(
    private val api: CoinApi
) {
    fun getCoins(): Flow<Resource<List<CoinDto>>> = flow {
        try {

            emit(Resource.Loading()) //indicar que estamos cargando

            val coins = api.getCoins() //descarga las monedas de internet, se supone quedemora algo

            emit(Resource.Success(coins)) //indicar que se cargo correctamente y pasarle las monedas
        } catch (e: HttpException) {
            //error general HTTP
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            //debe verificar tu conexion a internet
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }
}

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinDto> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val coinsRepository: CoinsRepository
) : ViewModel() {

    private var _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    init {
        coinsRepository.getCoins().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = CoinListState(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = CoinListState(coins = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = CoinListState(error = result.message ?: "Error desconocido")
                }
            }
        }.launchIn(viewModelScope)
    }
}