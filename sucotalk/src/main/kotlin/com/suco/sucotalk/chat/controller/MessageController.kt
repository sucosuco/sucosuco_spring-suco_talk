package com.suco.sucotalk.chat.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class MessageController(private val messagingTemplate: SimpMessageSendingOperations) {

    @MessageMapping("/chat/message")
    fun handleMessage(message: String) {
        messagingTemplate.convertAndSend("/sub/chat/room/", message)
    }
}