package com.suco.sucotalk.room.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomRepositoryImpl: RoomRepositoryImpl) {

    fun enter(member: Member, roomId: Long) {
//        roomRepositoryImpl.enterRoom(member, roomId)
    }

    fun create(room: Room): Room {
        return roomRepositoryImpl.save(room)
    }

    fun sendDirectMessage(sender: Member, receiver: Member, message: String) {
        val dmRoom = findDirectRoom(sender, receiver) ?: create(Room(members = mutableListOf(sender, receiver)))
        val message = Message(sender = sender, room = dmRoom, content = message)

    }

    fun findDirectRoom(sender: Member, receiver: Member): Room? {
        val dmRooms1 = roomRepositoryImpl.findEnteredRoom(sender).filter { it.isDm() }
        val dmRooms2 = roomRepositoryImpl.findEnteredRoom(receiver).filter { it.isDm() }

        return dmRooms1.find { dmRooms2.contains(it) }
    }
}