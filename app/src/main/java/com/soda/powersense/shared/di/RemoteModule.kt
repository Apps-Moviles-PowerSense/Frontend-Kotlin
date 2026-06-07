package com.soda.powersense.shared.di

import com.soda.powersense.alerts.data.remote.AlertService
import com.soda.powersense.auth.data.local.UserDao
import com.soda.powersense.auth.data.remote.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String =
        //"http://<Dirección IPv4>:8080/api/"
        "http://192.168.1.135:8080/api/"

    @Provides
    @Singleton
    fun providesOkHttpClient(userDao: UserDao): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val user = runBlocking { userDao.getUser().firstOrNull() }
                val request = chain.request().newBuilder()
                user?.token?.let {
                    request.addHeader("Authorization", "Bearer $it")
                }
                chain.proceed(request.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        @Named("baseUrl") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providesAlertService(retrofit: Retrofit): AlertService =
        retrofit.create(AlertService::class.java)

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}
