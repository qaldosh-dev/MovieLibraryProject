package com.example.movielibraryproject.data.remote

import com.example.movielibraryproject.BuildConfig
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbClient {
    val retrofit: Retrofit by lazy { createRetrofit() }

    fun createRetrofit(readAccessToken: String = BuildConfig.TMDB_READ_ACCESS_TOKEN): Retrofit {
        val token = readAccessToken.trim()
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(45, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                if (token.isBlank()) {
                    throw IOException(
                        "TMDB access is not configured. Set TMDB_READ_ACCESS_TOKEN " +
                            "in local.properties and rebuild the app."
                    )
                }
                if (token.any { it <= ' ' || it >= '\u007f' }) {
                    throw IOException(
                        "TMDB_READ_ACCESS_TOKEN is invalid. Use only the API Read Access Token " +
                            "without the Bearer prefix."
                    )
                }
                val request = chain.request().newBuilder()
                    .header("Authorization", "Bearer $token")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
