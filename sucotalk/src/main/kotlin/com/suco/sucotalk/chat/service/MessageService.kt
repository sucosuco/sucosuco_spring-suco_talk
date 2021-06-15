package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.chat.dto.SendMessageDto
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class MessageService(private val roomRepository: RoomRepositoryImpl, private val memberDao: MemberDao, private val messageDao: MessageDao) {

    fun save(sendMessage: SendMessageDto): MessageDto {
        val room = roomRepository.findById(sendMessage.roomId)
        val sender = memberDao.findByName(sendMessage.senderName!!)
//        val message = Message(room = room, sender = sender, content = sendMessage.contents, time = sendMessage.sendTime);
        val message = Message(room = room, sender = sender, content = sendMessage.contents)
        return MessageDto.of(messageDao.save(message))
    }

    fun findAllInRoom(room: Room): List<MessageDto> {
        return MessageDto.listOf(messageDao.findByRoom(room))
    }

}