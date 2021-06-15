package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.dto.MessageResponse
import com.suco.sucotalk.chat.dto.MessageRequest
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class MessageService(private val roomRepository: RoomRepositoryImpl, private val memberDao: MemberDao, private val messageDao: MessageDao) {

    fun save(message: MessageRequest): MessageResponse {
        val room = roomRepository.findById(message.roomId)
        val sender = memberDao.findByName(message.senderName!!)
//        val message = Message(room = room, sender = sender, content = message.contents, time = message.sendTime);
        val message = Message(room = room, sender = sender, content = message.contents)
        return MessageResponse.of(messageDao.save(message))
    }

    fun findAllInRoom(room: Room): List<MessageResponse> {
        return MessageResponse.listOf(messageDao.findByRoom(room))
    }

}