package com.suco.sucotalk.room.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class RoomRepositoryImpl(private val roomDao: RoomDao, private val memberDao: MemberDao) {

    fun save(room: Room): Room {
        val savedId = roomDao.create(room)
        return findById(savedId)
    }

    fun findById(id: Long): Room {
        val room: Room = roomDao.findById(id)
        val participants: List<Long> = roomDao.findParticipantsById(id)
        val members: MutableList<Member> = memberDao.findByIds(participants)
        return Room(room.id, room.name, members)
    }

    fun findAll(): List<Room> {
        val rooms = roomDao.getAllRoom()
        return rooms.map { findById(it.id!!) }
    }

    fun findEnteredRooms(member: Member): List<Room> {
        val ids = roomDao.findRoomByMember(member)
        return ids.map { findById(it) }
    }

    fun findAccessibleRooms(member: Member): List<Room> {
        val rooms = roomDao.getAllRoom()
        val ids = roomDao.findRoomByMember(member)

        return rooms.stream()
            .filter { room -> !ids.contains(room.id) }
            .collect(Collectors.toList())
    }

    fun deleteMemberInRoom(room: Room, member: Member) {
        roomDao.deleteMemberInRoom(room, member)
    }

    fun insertMemberInRoom(room: Room, member: Member) {
        roomDao.insertMemberInRoom(room, member)
    }
}