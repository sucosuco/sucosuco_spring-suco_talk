package com.suco.sucotalk.room.service

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.chat.service.MessageService
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.dto.RoomCreateRequest
import com.suco.sucotalk.room.dto.RoomCreateResponse
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class RoomService(
        private val messageService: MessageService,
        private val roomRepositoryImpl: RoomRepositoryImpl,
        private val memberDao: MemberDao
) {

    fun rooms(): List<RoomDto> {
        val rooms = roomRepositoryImpl.findAll()
        return RoomDto.listOf(rooms)
    }

    fun myRooms(userName: String): List<RoomDto> {
        val user = memberDao.findByName(userName);
        val rooms = roomRepositoryImpl.findEnteredRooms(user)
        return RoomDto.listOf(rooms)
    }

    fun accessibleRooms(userName: String): List<RoomDto> {
        val user = memberDao.findByName(userName);
        val rooms = roomRepositoryImpl.findAccessibleRooms(user)
        return RoomDto.listOf(rooms)
    }

    fun exit(memberName: String, roomId: Long) {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.exit(member)
        roomRepositoryImpl.deleteMemberInRoom(room, member)
    }

    fun enter(memberName: String, roomId: Long): List<MessageDto> {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.enter(member)
        roomRepositoryImpl.insertMemberInRoom(room, member)
        return messageService.findAllInRoom(room)
    }

    fun enterNewRoom(memberIds: List<Long>): List<MessageDto> {
        val members = memberIds.map { memberDao.findById(it) }

        if (members.size == 2) {
            val dmRoom = findDirectRoom(members[0], members[1]) ?: createNewRoom(members)
            return messageService.findAllInRoom(dmRoom)
        }

        val dmRoom = createNewRoom(members)
        return messageService.findAllInRoom(dmRoom)
    }

    fun sendMessage(sender: Member, roomId: Long, message: String) {
        sendMessage(sender, roomRepositoryImpl.findById(roomId), message)
    }

    fun sendMessage(sender: Member, room: Room, message: String) {
        val message = Message(sender = sender, room = room, content = message)
        messageService.save(message)
//        socketService.send(message)
    }

    fun sendDirectMessage(sender: Member, receiver: Member, message: String) {
        val dmRoom = findDirectRoom(sender, receiver)
                ?: createNewRoom(mutableListOf(sender, receiver))

        sendMessage(sender, dmRoom, message)
    }

    fun createRoom(roomInfo: RoomCreateRequest): RoomCreateResponse {
        val members = memberDao.findByIds(roomInfo.members)
        val savedRoom = roomRepositoryImpl.save(Room(name = roomInfo.name, members = members))
        return RoomCreateResponse.of(savedRoom)
    }

    fun createRoom(masterName: String, roomInfo: RoomCreateRequest): RoomCreateResponse {
        val master = memberDao.findByName(masterName);
        val members = memberDao.findByIds(roomInfo.members)
        members.add(master);
        val savedRoom = roomRepositoryImpl.save(Room(name = roomInfo.name, members = members))
        return RoomCreateResponse.of(savedRoom)
    }

    private fun createNewRoom(members: List<Member>): Room {
        return roomRepositoryImpl.save(Room(members = members))
    }

    private fun findDirectRoom(sender: Member, receiver: Member): Room? {
        val dmRooms1 = roomRepositoryImpl.findEnteredRooms(sender).filter { it.isDm() }
        val dmRooms2 = roomRepositoryImpl.findEnteredRooms(receiver).filter { it.isDm() }

        return dmRooms1.find { dmRooms2.contains(it) }
    }

    fun roomDetail(id: Long): RoomDetail {
        val room: Room = roomRepositoryImpl.findById(id)
        val messages: List<MessageDto> = messageService.findAllInRoom(room)
        return RoomDetail.of(room, messages)
    }
}