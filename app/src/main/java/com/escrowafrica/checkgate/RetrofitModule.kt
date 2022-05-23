package com.escrowafrica.checkgate

import android.content.Context
import com.escrowafrica.checkgate.ui.network.SessionManager
import com.escrowafrica.checkgate.ui.network.TokenInterceptor
import com.escrowafrica.checkgate.ui.network.retrofit.CheckGateApis
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun providesGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun providesInterceptors() : OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(TokenInterceptor())
        }.build()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(client :OkHttpClient , gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://checkgate.herokuapp.com/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun providesApiService (retrofit :  Retrofit.Builder) : CheckGateApis{
        return retrofit.build().create(CheckGateApis::class.java)
    }


    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) : SessionManager {
        return SessionManager(context)
    }


}