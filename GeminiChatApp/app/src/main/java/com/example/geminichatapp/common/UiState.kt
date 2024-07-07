package com.example.geminichatapp.common

import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity

data class UiState(
    val isInitialState: Boolean? = false,
    val isLoading: Boolean? = false,
    var chatList: List<MessageEntity>? = mutableListOf(),
    var chatListWithDate: Map<String, List<MessageEntity>>? = mutableMapOf(),
    var channelList: List<ChannelWithMessage>? = mutableListOf(),
    val error: Throwable? = Throwable(message = "Something went wrong"),
)