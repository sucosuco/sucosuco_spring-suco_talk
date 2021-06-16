package com.suco.sucotalk.room.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.dto.RoomRequest
import com.suco.sucotalk.room.exception.RoomException
import com.suco.sucotalk.room.repository.RoomRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class RoomServiceTest {

    @Autowired
    lateinit var roomRepositoryImpl: RoomRepositoryImpl

    @Autowired
    lateinit var roomService: RoomService

    @Autowired
    lateinit var memberDao: MemberDao

    lateinit var savedMember1: Member
    lateinit var savedMember2: Member
    lateinit var savedMember3: Member
    lateinit var savedMember4: Member

    @BeforeEach
    fun initMemberAndRoom() {
        val savedMemberId1 = memberDao.insert("test1", "test")
        val savedMemberId2 = memberDao.insert("test2", "test")
        val savedMemberId3 = memberDao.insert("test3", "test")
        val savedMemberId4 = memberDao.insert("test4", "test")

        savedMember1 = memberDao.findById(savedMemberId1)
        savedMember2 = memberDao.findById(savedMemberId2)
        savedMember3 = memberDao.findById(savedMemberId3)
        savedMember4 = memberDao.findById(savedMemberId4)
    }

    @DisplayName("enterNewRoom :: 두 명 이상의 유저가 방을 정상 생성한다.")
    @Test
    fun createNewRoomTest() {
        val createdRoom: Room = createRoom(savedMember1, listOf(savedMember2, savedMember3))

        assertMembersInRoom(createdRoom, savedMember1, savedMember2, savedMember3)
    }

    @DisplayName("enter :: 유저가 방에 정상 입장한다.")
    @Test
    fun enterRoomTest() {
        val room: Room = createRoom(savedMember1, listOf(savedMember2, savedMember3))
        val enteredRoom = enterRoom(room, savedMember4)
        assertMembersInRoom(enteredRoom, savedMember1, savedMember2, savedMember3, savedMember4)
    }

    @DisplayName("exit :: 유저가 방에 정상 퇴장한다.")
    @Test
    fun exitRoomTest() {
        val room: Room = createRoom(savedMember1, listOf(savedMember2, savedMember3))
        val afterExit: Room = exitRoom(room, savedMember1)

        assertThat(afterExit.members.contains(savedMember1)).isFalse
        assertMembersInRoom(afterExit, savedMember2, savedMember3)
    }

    @DisplayName("exit :: 멤버가 홀로 남은 방은 삭제된다.")
    @Test
    fun existDirectRoom() {
        assertThrows<RoomException> {
            val room: Room = createRoom(savedMember1, listOf(savedMember2))
            exitRoom(room, savedMember1)
        }
    }

    @DisplayName("exception :: 홀로 방을 생성할 수 없다.")
    @Test
    fun createRoomWithOnlyMember() {
        assertThrows<RoomException> {
            createRoom(savedMember1, listOf())
        }
    }

    @DisplayName("exception :: 중복된 유저가 방에 있을 수 없다.")
    @Test
    fun enterRoomWithDuplicatedMembers() {
        assertThrows<RoomException> {
            createRoom(savedMember1, listOf(savedMember1, savedMember3))
        }
    }

    @DisplayName("exception :: 존재하지 않은 유저가 방에 나갈 수 없다.")
    @Test
    fun exitRoomNonExistenceMember() {
        assertThrows<RoomException> {
            val createdRoom: Room = createRoom(savedMember1, listOf(savedMember2, savedMember3))
            exitRoom(createdRoom, savedMember4)
        }
    }

    private fun createRoom(master: Member, members: List<Member>): Room {
        roomService.createRoom(master.name, RoomRequest("testRoom", members.map { it.id }))
        return roomRepositoryImpl.findEnteredRooms(savedMember1).first()
    }

    private fun enterRoom(room: Room, member: Member): Room {
        roomService.enter(member.name, room.id!!)
        return roomRepositoryImpl.findEnteredRooms(member).first()
    }

    private fun exitRoom(room: Room, member: Member): Room {
        roomService.exit(member.name, room.id!!)
        return roomRepositoryImpl.findById(room.id!!)
    }

    private fun assertMembersInRoom(room: Room, vararg members: Member) {
        val memberIds = room.members.map { it.id }

        assertThat(memberIds).hasSize(members.size)
        for (member in members) {
            assertThat(memberIds).contains(member.id)
        }
    }
}