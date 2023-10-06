package com.crystal.composedi.service

import com.crystal.composedi.model.Repo
import dagger.Provides
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String): List<Repo>
}