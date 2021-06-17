package com.suco.sucotalk.room.service

import com.suco.sucotalk.chat.dto.MessageResponse
import com.suco.sucotalk.chat.service.MessageService
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
        val user = memberDao.findByName(userName)
        return RoomApproximate.listOf(roomRepositoryImpl.findAccessibleRooms(user))
    }

    fun exit(memberName: String, roomId: Long) {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.exit(member)
        roomRepositoryImpl.deleteMemberInRoom(room, member)

        if (room.end) {
            deleteRoom(room)
        }
    }

    private fun deleteRoom(room: Room) {
        messageService.deleteAllInRoom(room)
        roomRepositoryImpl.remove(room)
    }

    fun enter(memberName: String, roomId: Long): List<MessageResponse> {
        val member = memberDao.findByName(memberName)
        val room = roomRepositoryImpl.findById(roomId)
        room.enter(member)

        roomRepositoryImpl.insertMemberInRoom(room, member)
        return messageService.findAllInRoom(room)
    }

    fun createRoom(masterName: String, roomInfo: RoomRequest): RoomApproximate {
        val master = memberDao.findByName(masterName)
        val members = memberDao.findByIds(roomInfo.members).plus(master)
        val room = Room(roomInfo.name, members)

        require(!roomRepositoryImpl.isExisting(room)) { throw RoomException("중복된 이름의 방을 생성할 수 없습니다.") }
        require(members.size >= 2) { throw RoomException("방 생성 최소 인원은 2명입니다.") }
        return RoomApproximate.of(roomRepositoryImpl.save(room))
    }

    fun roomDetail(id: Long): RoomDetail {
        val room: Room = roomRepositoryImpl.findById(id)
        return RoomDetail.of(room, messageService.findAllInRoom(room))
    }
}
