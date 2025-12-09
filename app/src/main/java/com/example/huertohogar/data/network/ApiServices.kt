package com.example.huertohogar.data.network

import com.example.huertohogar.data.db.ProductEntity
import com.example.huertohogar.data.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface HuertoApiService {

    @GET("api/categorias")
    suspend fun getCategories(): List<Category>

    @GET("api/huerto/productos")
    suspend fun getProducts(): List<ProductEntity>

    @GET("api/huerto")
    suspend fun getHuerto(): List<Category>

    @GET("api/usuarios")
    suspend fun getUsers(): List<UserDto>

    // --- Autenticación ---

    @POST("api/usuarios/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("api/usuarios")
    suspend fun register(@Body body: RegisterRequest): Response<Void>

    // --- Stock ---

    @PATCH("api/productos/{id}/stock")
    suspend fun updateStock(
        @Path("id") id: String,
        @Body body: StockRequest
    ): Response<Unit>
}

object RetrofitInstance {

    private const val BASE_URL = "https://api-dfs2-dm-production.up.railway.app/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
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