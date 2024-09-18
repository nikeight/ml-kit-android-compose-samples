package com.example.geminichatapp.common

import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.features.text_chat.Message


data class UiState(
    var chatListWithDate: MutableMap<String, MutableList<Message>>? = mutableMapOf(),
    var channelList: List<ChannelWithMessage>? = mutableListOf(),
    var loading: Boolean = false,
    var isError : Boolean = false
)