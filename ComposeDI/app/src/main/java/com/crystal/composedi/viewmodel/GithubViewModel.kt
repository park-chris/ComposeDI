package com.crystal.composedi.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystal.composedi.model.Repo
import com.crystal.composedi.service.GithubService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val githubService: GithubService
) : ViewModel() {
    val repos = mutableStateListOf<Repo>()

    fun getRepos() {
        repos.clear()
        viewModelScope.launch {
            val result = githubService.listRepos("park-chris")
            repos.addAll(result)
        }
    }
}