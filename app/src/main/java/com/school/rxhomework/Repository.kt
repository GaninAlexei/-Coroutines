package com.school.rxhomework

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Repository {

    suspend fun getPosts(): Flow<List<MainActivity.Adapter.Item>> = NetworkSource.getPosts()

    object NetworkSource {
        private interface IPostApi {
            @GET("/posts")
            suspend fun getPosts(): List<MainActivity.Adapter.Item>
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        private val postApi = retrofit.create(IPostApi::class.java)

        suspend fun getPosts(): Flow<List<MainActivity.Adapter.Item>> {
            return flow {
                emit(postApi.getPosts())
            }
        }
    }
}
