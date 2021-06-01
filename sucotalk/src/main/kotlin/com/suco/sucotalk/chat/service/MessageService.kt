package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.repository.MessageDao
import org.springframework.stereotype.Service

@Service
class MessageService(private val messageDao: MessageDao) {

    fun save(message: Message) {
        messageDao.save(message)
    }

}