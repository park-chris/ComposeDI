package com.crystal.helloworld.di

import com.crystal.helloworld.service.GithubService
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
// 의존성 주입을 하고 있는 Provide가 있는 공간
@InstallIn(SingletonComponent::class)
@Module
class AppModules {
    // 단계 5: 아래 프로파이더를 만듭시다.
    @Singleton // 한번만 생성하도록 하겠다
    @Provides // 주입을 하겠다
    @Named("API_URI") // 함수 이름은 Dagger Hilt에서는 의미가 없고 같은 타입이 여러개 일수도 있어서 Named로 구분할 수 있게 붙여줌
    fun provideWebAPI(): String = "https://api.github.com/"

    // JSON <-> 코틀린 객체, 자바 객체
    // FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES : 상대방이 보내오는 거에 _(언더바)가 있을수도 있다고 세팅
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