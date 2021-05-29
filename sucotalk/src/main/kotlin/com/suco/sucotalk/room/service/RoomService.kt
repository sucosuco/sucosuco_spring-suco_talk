package com.suco.sucotalk.room.service

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

    fun sendDirectMessage(member1: Member, member2: Member) {
        val dmRoom = findDirectRoom(member1, member2) ?: create(Room(members = mutableListOf(member1, member2)))
    }

    fun findDirectRoom(member1: Member, member2: Member): Room? {
        val dmRooms1 = roomRepositoryImpl.findEnteredRoom(member1).filter { it.isDm() }
        val dmRooms2 = roomRepositoryImpl.findEnteredRoom(member2).filter { it.isDm() }

        return dmRooms1.find { dmRooms2.contains(it) }
    }
}