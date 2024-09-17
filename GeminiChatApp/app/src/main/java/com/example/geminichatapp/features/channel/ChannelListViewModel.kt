package com.example.geminichatapp.features.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState(isInitialState = true))

    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    init {
        fetchChannelsList()
    }

    private fun fetchChannelsList() {
        _uiState.value = UiState(
            isLoading = true
        )
        viewModelScope.launch {
            repository.fetchChannels()
                .collect {
                    _uiState.value = _uiState.value.copy(
                        channelList = it
                    )
                }
        }
    }

    // TODO: Implement the sorting
    fun sortChatViaDates(){

    }
}