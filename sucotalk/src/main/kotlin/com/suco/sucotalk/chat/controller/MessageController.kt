package com.suco.sucotalk.chat.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.chat.dto.MessageRequest
import com.suco.sucotalk.chat.service.MessageService
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest
import javax.servlet.http.HttpServletRequest

@Controller
class MessageController(private val messagingTemplate: SimpMessageSendingOperations, private val authService: AuthService,
                        private val messageService: MessageService) {

    @MessageMapping("/chat/message")
    fun sendMessage(@Header(value = "Authorization") token: String, message: MessageRequest) {
        val sender = authService.getPayloadFromBearer(token)
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.roomId, messageService.send(message, sender))
    }
}