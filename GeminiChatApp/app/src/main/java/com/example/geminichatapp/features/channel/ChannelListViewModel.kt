package com.example.geminichatapp.features.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> =
        _uiState
            .onStart {
                fetchChannelsList()
            }
            .stateIn(
                scope = viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
                initialValue = UiState()
            )

    private fun fetchChannelsList() =
        viewModelScope.launch {
            repository.fetchChannels()
                .collect {
                    _uiState.value = _uiState.value.copy(
                        channelList = it
                    )
                }
        }


    // TODO: Implement the sorting
    fun sortChatViaDates() {

    }
}