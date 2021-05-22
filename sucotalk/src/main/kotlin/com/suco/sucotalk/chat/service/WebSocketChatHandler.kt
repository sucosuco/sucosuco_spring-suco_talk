package com.suco.sucotalk.chat.service

import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketChatHandler : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        print(payload)
        val textMessage = TextMessage("Welcome suco talk")
        session.sendMessage(textMessage)
    }
}