package com.suco.sucotalk.room.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import org.springframework.stereotype.Repository

@Repository
class RoomRepositoryImpl(private val roomDao: RoomDao, private val memberDao: MemberDao) {

    fun save(room: Room) :Room{
        val savedId = roomDao.create(room)
        val savedRoom = findById(savedId) /// 참여자 정보를 가져
        return savedRoom
    }

    fun findById(id: Long): Room {
        val room: Room = roomDao.findById(id)
        val participants: List<Long> = roomDao.findParticipantsById(id)
        val members: MutableList<Member> = memberDao.findByIds(participants)
        return Room(room.id, room.name, members)
    }

    fun findEnteredRoom(member: Member): List<Room> {
        val ids = roomDao.findRoomByMember(member)
        return ids.map { findById(it) }
    }
}