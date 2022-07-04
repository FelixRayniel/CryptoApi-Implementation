package com.example.cryptoapi_implementation.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface CoinApi {
    @GET("/Coins")
    suspend fun getCoins(): List<CoinDto>

    @POST("/Coins")
    suspend fun PostInser(@Body coinDto: CoinDto): CoinDto

}