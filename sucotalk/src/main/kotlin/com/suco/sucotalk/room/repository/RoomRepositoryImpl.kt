package com.suco.sucotalk.room.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import org.springframework.stereotype.Repository

@Repository
class RoomRepositoryImpl(val roomDao: RoomDao, val memberDao: MemberDao) {

    fun save(room: Room) :Room{
        val create = roomDao.create(room)
        return findById(create)
    }

    fun findById(id: Long): Room {
        val room: Room = roomDao.findById(id)
        val participants: List<Long> = roomDao.findParticipantsById(id)
        val members: MutableList<Member> = memberDao.findByIds(participants)

        return Room(room.id, room.name, members)
    }

    fun findEnteredRoom(member: Member): List<Room> {
        val ids = roomDao.findEnteredRoomIds(member)
        return ids.map { findById(it) }
    }
}