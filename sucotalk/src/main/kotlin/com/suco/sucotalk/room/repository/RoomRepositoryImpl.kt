package com.suco.sucotalk.room.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.domain.RoomInfo
import org.springframework.stereotype.Repository

@Repository
class RoomRepositoryImpl(private val roomDao: RoomDao, private val memberDao: MemberDao) {

    fun save(room: Room): Room {
        val savedId = roomDao.create(room)
        return findById(savedId)
    }

    fun findById(id: Long): Room {
        val room: RoomInfo = roomDao.findById(id)
        val participants: List<Long> = roomDao.findParticipantsById(id)
        val members: List<Member> = memberDao.findByIds(participants)
        return Room(room.id, room.name, members)
    }

    fun isExisting(room: Room): Boolean {
        return roomDao.isExistingName(room.name)
    }

    fun findAll(): List<Room> {
        val rooms = roomDao.getAllRoom()
        return rooms.map { findById(it.id!!) }
    }

    fun findEnteredRooms(member: Member): List<Room> {
        val ids = roomDao.findRoomByMember(member)
        return ids.map { findById(it) }
    }

    fun findAccessibleRooms(member: Member): List<RoomInfo> {
        val rooms: List<RoomInfo> = roomDao.getAllRoom()
        val ids = roomDao.findRoomByMember(member)

        return rooms.filter { room -> !ids.contains(room.id) }
    }

    fun deleteMemberInRoom(room: Room, member: Member) {
        roomDao.deleteMemberInRoom(room, member)
    }

    fun insertMemberInRoom(room: Room, member: Member) {
        roomDao.insertMemberInRoom(room, member)
    }

    fun remove(room: Room) {
        roomDao.deleteAllParticipants(room)
        roomDao.delete(room)
    }
}