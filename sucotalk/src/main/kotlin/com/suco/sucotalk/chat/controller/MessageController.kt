package com.suco.sucotalk.chat.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.chat.dto.SendMessageDto
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.chat.service.MessageService
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class MessageController(private val messagingTemplate: SimpMessageSendingOperations, private val authService: AuthService,
                        private val messageService: MessageService) {

    @MessageMapping("/chat/message")
    fun handleMessage(@Header(value = "Authorization") token:String, message: SendMessageDto) {
        val userName = authService.getPayloadFromBearer(token)
        val sendMessageDto = SendMessageDto(roomId = message.roomId, senderName = userName, contents = message.contents)
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.roomId,  messageService.save(sendMessageDto))
    }

//     TODO :: messageService에 역할을 넘길 순 없을까
//     ex, 방에 존재하는 유저인지 확인 등
//    @MessageMapping("/chat/message")
//    fun handleMessageTemp(@Header(value = "Authorization") token:String, message: SendMessageDto) {
//        val userName = authService.getPayloadFromBearer(token)
//        val sendMessageDto = SendMessageDto(message.roomId, userName, message.contents)
//        val dest = "/sub/chat/room/" + message.roomId
//
//        messageService.send(dest, sendMessageDto)
//    }
}