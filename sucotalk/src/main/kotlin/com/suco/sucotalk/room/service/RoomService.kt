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
        return RoomDto.listOf(roomRepositoryImpl.getAllRoom())
    }

    fun exit(memberName: String, roomId: Long): Member {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.exit(member)
        roomRepositoryImpl.deleteMemberInRoom(room, member)
        return member
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

    private fun createNewRoom(members: List<Member>): Room {
        return roomRepositoryImpl.save(Room(members = members))
    }

    private fun findDirectRoom(sender: Member, receiver: Member): Room? {
        val dmRooms1 = roomRepositoryImpl.findEnteredRoom(sender).filter { it.isDm() }
        val dmRooms2 = roomRepositoryImpl.findEnteredRoom(receiver).filter { it.isDm() }

        return dmRooms1.find { dmRooms2.contains(it) }
    }

    fun roomDetail(id :Long): RoomDetail {
        val room : Room = roomRepositoryImpl.findById(id)
        val messages :List<MessageDto> =  messageService.findAllInRoom(room)
        return RoomDetail(room, messages)
    }
}