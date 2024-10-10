package com.example.geminichatapp.features.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.repo.MessageState
import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.di.DispatcherProvider
import com.example.geminichatapp.util.getDateAsYearMonthDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> =
        _uiState.onStart {
            collectLatestMsg()
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = UiState()
        )

    private var _channelId: UUID? = null

    // Todo : Make it MVI based arch,
    //  Single Interface method which send the text and image
    fun sendMessage(
        messageEntity: MessageEntity,
    ) {
        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
            repository.sendMessage(messageEntity)
                .collectLatestAndUpdateState(state = _uiState)
        }
    }

    fun setChannelId(passedChannelId: UUID) {
        _channelId = passedChannelId
    }

    //TODO : To much of repeating loops, optimize it
    private fun collectLatestMsg() {
        // Convert list of Item to Map
        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
            _channelId?.let { safeChannelId ->
                repository.fetchAllHistory(safeChannelId)
                    .distinctUntilChanged()
                    .collectLatest { newMessage ->
                        newMessage?.let { newMessagesList ->
                            val map = mutableMapOf<String, MutableList<Message>>()
                            newMessagesList.map { newMessage ->
                                map.getOrPut(
                                    newMessage.date
                                        .getDateAsYearMonthDay(),
                                ) { mutableListOf() }
                                    .also {
                                        it.add(newMessage)
                                    }
                            }
                            _uiState.update { state ->
                                state.copy(
                                    chatListWithDate = map,
                                )
                            }
                            if (newMessage.isNotEmpty()) {
                                updateChannel(
                                    newMessagesList.last().id,
                                    messageLastDate = newMessagesList.last().date
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun updateChannel(
        messageId: Int?,
        messageLastDate: Date?,
    ) {
        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
            _channelId?.let { safeChannelId ->
                messageId?.let { safeMsgId ->
                    repository.updateChannel(
                        channelEntity = ChannelEntity(
                            channelId = safeChannelId,
                            safeMsgId,
                            messageLastDate
                        )
                    )
                }
            }

        }
    }

    fun sendImagePrompt(messageEntity: MessageEntity) {
        viewModelScope.launch(dispatcherProvider.mainDispatcher){
            repository.sendImagePrompt(messageEntity)
                .collectLatestAndUpdateState(state = _uiState)
        }
    }
}

suspend fun Flow<MessageState>.collectLatestAndUpdateState(state: MutableStateFlow<UiState>) {
    this.collectLatest { msgState ->
        when (msgState) {
            is MessageState.Loading -> {
                state.update {
                    it.copy(
                        loading = true,
                        isError = false
                    )
                }
            }

            is MessageState.Success -> {
                state.update {
                    it.copy(
                        loading = false,
                        isError = false
                    )
                }
            }

            is MessageState.Failure -> {
                state.update {
                    it.copy(
                        loading = false,
                        isError = true
                    )
                }
            }
        }
    }
}