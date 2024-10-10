package com.example.geminichatapp.data.model

import com.example.geminichatapp.features.chat.Message

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        time = time,
        date = date,
        images = images,
        message = message,
        byWhom = byWhom,
        foreignChannelId = foreignChannelId,
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        time = time,
        date = date,
        images = images,
        message = message,
        byWhom = byWhom,
        foreignChannelId = foreignChannelId,
    )
}