package com.suco.sucotalk.room.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.member.service.MemberService

import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomRepositoryImpl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
    lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberDao: MemberDao

    private lateinit var testMember1: Member
    private lateinit var testMember2: Member

    @BeforeEach
    fun init() {
        val memberId1 = memberDao.insert(Member("corgi", "test"))
        testMember1 = memberDao.findById(memberId1)

        val memberId2 = memberDao.insert(Member("suri", "test"))
        testMember2 = memberDao.findById(memberId2)
    }


    lateinit var savedMember1: Member
    lateinit var savedMember2: Member
    lateinit var savedMember3: Member

    @BeforeEach
    fun initMemberAndRoom() {
        val savedMemberId1 = memberService.createMember("test1", "test")
        val savedMemberId2 = memberService.createMember("test2", "test")
        val savedMemberId3 = memberService.createMember("test3", "test")

        savedMember1 = memberService.findById(savedMemberId1)
        savedMember2 = memberService.findById(savedMemberId2)
        savedMember3 = memberService.findById(savedMemberId3)
    }

    @DisplayName("enterNewRoom :: 두 명 이상의 유저가 방을 정상 생성한다.")
    @Test
    fun createNewRoomTest() {
        val createdRoom: Room = createRoom(listOf(savedMember1, savedMember2))

        assertMembersInRoom(createdRoom, savedMember1, savedMember2)
    }

    @DisplayName("enter :: 유저가 방에 정상 입장한다.")
    @Test
    fun enterRoomTest() {
        val createdRoom: Room = createRoom(listOf(savedMember1, savedMember2))
        val enteredRoom = enterRoom(createdRoom, savedMember3)

        assertMembersInRoom(enteredRoom, savedMember1, savedMember2, savedMember3)
    }

    @DisplayName("exit :: 유저가 방에 정상 퇴장한다.")
    @Test
    fun exitRoomTest() {
        val createdRoom: Room = createRoom(listOf(savedMember1, savedMember2))
        val enteredRoom = enterRoom(createdRoom, savedMember3)
        val afterExit = exitRoom(enteredRoom, savedMember1)

        assertMembersInRoom(afterExit, savedMember2, savedMember3)
    }

    private fun createRoom(members: List<Member>): Room {
        roomService.enterNewRoom(members.map { it.id })
        return roomRepositoryImpl.findEnteredRoom(savedMember1).first()
    }

    private fun enterRoom(room: Room, member: Member): Room {
        roomService.enter(member.id, room.id!!)
        return roomRepositoryImpl.findEnteredRoom(member).first()
    }

    private fun exitRoom(room: Room, member: Member): Room {
        roomService.exit(member.id, room.id!!)
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