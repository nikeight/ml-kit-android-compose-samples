package com.example.geminichatapp.features.text_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.util.getDateAsYearMonthDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState(isInitialState = true))

    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private var _channelId: UUID? = null

    fun sendMessage(
        messageEntity: MessageEntity,
    ) {
        viewModelScope.launch {
            repository.sendMessage(messageEntity)
        }
    }

    fun setChannelId(passedChannelId: UUID) {
        _channelId = passedChannelId
        collectLatestMsg()
    }

    private fun collectLatestMsg() {
        viewModelScope.launch {
            _channelId?.let { safeChannelId ->
                repository.fetchAllHistory(safeChannelId)
                    .distinctUntilChanged()
                    .collectLatest { newMessage ->
                        newMessage?.let { newMessagesList ->
                            _uiState.value = _uiState.value.copy(
                                chatList = newMessagesList,
                            )
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

        // Convert list of Item to Map
        viewModelScope.launch {
            _channelId?.let { safeChannelId ->
                repository.fetchAllHistory(safeChannelId)
                    .distinctUntilChanged()
                    .collectLatest { newMessage ->
                        newMessage?.let { newMessagesList ->
                            newMessagesList.forEach {
                                _uiState.value = _uiState.value.copy(
                                    chatListWithDate = mutableMapOf<String, List<MessageEntity>>(
                                        it.date.getDateAsYearMonthDay() to newMessagesList
                                    )
                                )
                            }
                            _uiState.value = _uiState.value.copy(
                                chatList = newMessagesList,
                            )
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
        viewModelScope.launch {
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
        viewModelScope.launch {
            repository.sendImagePrompt(messageEntity)
        }
    }
}