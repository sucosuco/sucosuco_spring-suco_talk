package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.room.domain.Room
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageDao: MessageDao) {

    fun save(message: Message) : Message {
        return messageDao.save(message)
    }

    fun findAllInRoom(room: Room): List<Message> {
        return messageDao.findByRoom(room)
    }

}