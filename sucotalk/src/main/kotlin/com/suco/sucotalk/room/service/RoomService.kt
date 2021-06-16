package com.suco.sucotalk.room.service

import com.suco.sucotalk.chat.dto.MessageResponse
import com.suco.sucotalk.chat.service.MessageService
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.domain.RoomInfo
import com.suco.sucotalk.room.dto.RoomApproximate
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomRequest
import com.suco.sucotalk.room.exception.RoomException
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.springframework.stereotype.Service

@Service
class RoomService(
    private val messageService: MessageService,
    private val roomRepositoryImpl: RoomRepositoryImpl,
    private val memberDao: MemberDao
) {

    fun rooms(): List<RoomApproximate> {
        val rooms: List<Room> = roomRepositoryImpl.findAll()
        return RoomApproximate.listOf(rooms.map { RoomInfo(it) })
    }

    fun myRooms(userName: String): List<RoomApproximate> {
        val user = memberDao.findByName(userName);
        val rooms: List<Room> = roomRepositoryImpl.findEnteredRooms(user)
        return RoomApproximate.listOf(rooms.map { RoomInfo(it) })
    }

    fun accessibleRooms(userName: String): List<RoomApproximate> {
        val user = memberDao.findByName(userName);
        return RoomApproximate.listOf(roomRepositoryImpl.findAccessibleRooms(user))
    }

    fun exit(memberName: String, roomId: Long) {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.exit(member)
        roomRepositoryImpl.deleteMemberInRoom(room, member)
    }

    fun enter(memberName: String, roomId: Long): List<MessageResponse> {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.enter(member)
        roomRepositoryImpl.insertMemberInRoom(room, member)
        return messageService.findAllInRoom(room)
    }

    // TODO :: 네이밍 확인하기 -> dm 죽이기
    fun enterNewRoom(memberIds: List<Long>): List<MessageResponse> {
        val members = memberIds.map { memberDao.findById(it) }

        if (members.size == 2) {
            val dmRoom = findDirectRoom(members[0], members[1]) ?: createNewRoom(members)
            return messageService.findAllInRoom(dmRoom)
        }

        val newRoom = createNewRoom(members)
        return messageService.findAllInRoom(newRoom)
    }

    fun createRoom(masterName: String, roomInfo: RoomRequest): RoomApproximate {
        val master = memberDao.findByName(masterName)
        val members = memberDao.findByIds(roomInfo.members).plus(master)
        val room = Room(roomInfo.name, members)

        require(!roomRepositoryImpl.isExisting(room)) { throw RoomException("중복된 이름의 방을 생성할 수 없습니다.") }
        return RoomApproximate.of(roomRepositoryImpl.save(room))
    }

    private fun createNewRoom(members: List<Member>): Room {
        val roomName = members.map { it.name }.joinToString { "," }
        return roomRepositoryImpl.save(Room(roomName, members))
    }

    private fun findDirectRoom(sender: Member, receiver: Member): Room? {
        val dmRooms1 = roomRepositoryImpl.findEnteredRooms(sender).filter { it.isDm() }
        val dmRooms2 = roomRepositoryImpl.findEnteredRooms(receiver).filter { it.isDm() }

        return dmRooms1.find { dmRooms2.contains(it) }
    }

    fun roomDetail(id: Long): RoomDetail {
        val room: Room = roomRepositoryImpl.findById(id)
        val messages = messageService.findAllInRoom(room)
        return RoomDetail.of(room, messages)
    }

    //    fun sendMessage(sender: Member, roomId: Long, message: String) {
//        sendMessage(sender, roomRepositoryImpl.findById(roomId), message)
//    }
//
//    fun sendMessage(sender: Member, room: Room, message: String) {
//        val message = Message(sender = sender, room = room, content = message)
//        messageService.save(message)
////        socketService.send(message)
//    }
//
//    fun sendDirectMessage(sender: Member, receiver: Member, message: String) {
//        val dmRoom = findDirectRoom(sender, receiver)
//            ?: createNewRoom(mutableListOf(sender, receiver))
//
//        sendMessage(sender, dmRoom, message)
//    }
}