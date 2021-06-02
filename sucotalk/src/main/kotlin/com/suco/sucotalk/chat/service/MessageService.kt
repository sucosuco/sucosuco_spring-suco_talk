package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.room.domain.Room
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageDao: MessageDao) {

    fun save(message: Message): MessageDto {
        return MessageDto.of(messageDao.save(message))
    }

    fun findAllInRoom(room: Room): List<MessageDto> {
        return MessageDto.listOf(messageDao.findByRoom(room))
    }

}