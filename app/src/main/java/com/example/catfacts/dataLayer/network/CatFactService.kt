package com.example.catfacts.dataLayer.network

import com.example.catfacts.dataLayer.entities.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatFactService {
    @GET("fact")
    suspend fun fetchFact(): Response<Fact>
}