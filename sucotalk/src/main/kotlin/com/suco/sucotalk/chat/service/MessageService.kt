package com.suco.sucotalk.chat.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.dto.MessageRequest
import com.suco.sucotalk.chat.dto.MessageResponse
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.exception.RoomException
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class MessageService(private val roomRepository: RoomRepositoryImpl, private val memberDao: MemberDao, private val messageDao: MessageDao) {

    fun send(messageRequest: MessageRequest, senderName: String): MessageResponse {
        val room = roomRepository.findById(messageRequest.roomId)
        val sender = memberDao.findByName(senderName)
        validateMember(room, sender)

        val message = Message(room = room, sender = sender, content = messageRequest.contents)
        return MessageResponse.of(save(message))
    }

    fun findAllInRoom(room: Room): List<MessageResponse> {
        return MessageResponse.listOf(messageDao.findByRoom(room))
    }

    private fun validateMember(room: Room, sender: Member) {
        require(room.isParticipant(sender)) { throw RoomException("방에 존재하지 않는 사용자입니다.") }
    }

    private fun save(message: Message): Message {
        return messageDao.save(message);
    }
}