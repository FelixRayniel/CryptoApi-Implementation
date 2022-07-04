package com.example.cryptoapi_implementation.ui.theme.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun CoinRegistroScreen(
    navHostController: NavHostController,
    viewModel: CoinViewModel = hiltViewModel()
) {
    var error by remember {
        mutableStateOf(false)
    }

    val scaffoldState = rememberScaffoldState()

    var validar = LocalContext.current
    val focusRequesterDescripcion = FocusRequester()
    val focusRequesterPrecio = FocusRequester()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro  Criptomonedas",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigate(
                            "CoinListScreen"
                        )
                    }) {
                    }
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            Modifier
                .padding(it)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = viewModel.descripcion,
                label = {
                    Text(
                        text = "Moneda",
                        fontStyle = FontStyle.Normal
                    )
                },
                onValueChange = {
                    viewModel.descripcion = it
                    error = false
                },
                isError = error,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterDescripcion),
            )

            val assistiveElementText = if (error)
                "Error: Obligatorio" else "Obligatorio"
            val assistiveElementColor = if (error) {
                MaterialTheme.colors.error
            } else {
                MaterialTheme.colors
                    .onSurface
                    .copy(alpha = ContentAlpha.medium)
            }

            Text(
                text = assistiveElementText,
                color = assistiveElementColor,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )

            OutlinedTextField(
                value = viewModel.valor.toString(),
                label = {
                    Text(
                        text = "Valor Actual",
                        fontStyle = FontStyle.Normal
                    )
                },
                onValueChange = {
                    viewModel.valor = it.toDouble()
                    error = false
                },
                isError = error,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequesterPrecio),
                leadingIcon = {
                },
            )

            val assistiveText = if (error)
                "Error: Obligatorio" else "Obligatorio"
            val assistiveColor = if (error) {
                MaterialTheme
                    .colors
                    .error
            } else {
                MaterialTheme
                    .colors
                    .onSurface
                    .copy(alpha = ContentAlpha.medium)
            }

            Text(
                text = assistiveText,
                color = assistiveColor,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )



            Spacer(
                modifier = Modifier.width(20.dp)
            )
            Button(
                onClick = {
                    if (viewModel.descripcion.isNullOrEmpty()) {
                        error = viewModel.descripcion.isBlank()
                        Toast.makeText(
                            validar,
                            "Debe de ingresar un valor al campo Moneda",
                            Toast.LENGTH_LONG
                        ).show()
                        focusRequesterDescripcion.requestFocus()
                        return@Button
                    }

                    if (viewModel.valor <= 0) {
                        error = viewModel.valor.toString().isBlank()
                        Toast.makeText(
                            validar,
                            "Debe ingresar un valor al Valor Actual!",
                            Toast.LENGTH_LONG
                        ).show()
                        focusRequesterPrecio.requestFocus()
                        return@Button
                    }


                    viewModel.Guardar()

                    viewModel.recargarLista()

                    navHostController.navigate("CoinListScreen")
                    Toast.makeText(
                        validar,
                        "Se ha Guardado Correctamente!",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.descripcion = ""
                    viewModel.valor
                    //focusRequester.requestFocus()
                }
            ) {
                Text("  GUARDAR")
            }
        }
    }
}