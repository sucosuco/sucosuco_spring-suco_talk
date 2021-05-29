package com.suco.sucotalk.chat.service

data class ChatDto(
    private val type: String, // login, message
    private val sender: String,
    private val receiver: String,
    private val message: String
) {
    constructor(msg: List<String>) : this(msg[0], msg[1], msg[2], msg[3])
}