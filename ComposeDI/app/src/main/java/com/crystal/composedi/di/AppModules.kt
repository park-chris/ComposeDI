package com.crystal.composedi.di

import com.crystal.composedi.service.GithubService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


// 단계 4: `AppModules`에 `@Module` 어노테이션과 `@InstallIn(SingletonComponent::class)`
// 어노테이션을 추가합니다.
@InstallIn(SingletonComponent::class)
@Module
class AppModules {
    // 단계 5: 아래 프로파이더를 만듭시다.
    @Singleton // 한번만 생성
    @Provides // 여기서 주입을 하는거기 때문
    @Named("API_URI") // 함수이름은 내부적으로 의미가 없기 때문에 같은 타입이 여러개 있을 수 있으니 @Named를 세팅해줘야한다.
    fun provideWebAPI(): String = "https://api.github.com/"

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    @Singleton
    @Provides
    fun provideConverterFactory(
        gson: Gson
    ): Converter.Factory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("API_URI") apiUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(converterFactory)
        .build()

    @Singleton
    @Provides
    fun provideGithubService(
        retrofit: Retrofit
    ): GithubService = retrofit.create(GithubService::class.java)
}