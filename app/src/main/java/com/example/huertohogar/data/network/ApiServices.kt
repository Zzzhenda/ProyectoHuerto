package com.example.huertohogar.data.network

import com.example.huertohogar.data.db.ProductEntity
import com.example.huertohogar.data.model.Category
import com.example.huertohogar.data.model.LoginRequest
import com.example.huertohogar.data.model.LoginResponse
import com.example.huertohogar.data.model.StockRequest
import com.example.huertohogar.data.model.UserDto

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

import java.util.concurrent.TimeUnit

interface HuertoApiService {

    @GET("api/huerto/productos")
    suspend fun getProducts(): List<ProductEntity>

    @GET("api/huerto")
    suspend fun getHuerto(): List<Category>

    @GET("api/usuarios")
    suspend fun getUsers(): List<UserDto>

    @POST("api/usuarios/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @PATCH("api/productos/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: String,
        @Body body: StockRequest
    ): Response<Unit>
}

object RetrofitInstance {

    private const val BASE_URL =
        "https://api-dfs2-dm-production.up.railway.app/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: HuertoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HuertoApiService::class.java)
    }
}
