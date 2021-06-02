package com.suco.sucotalk.room.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
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
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var memberDao: MemberDao

    private lateinit var testMember1: Member
    private lateinit var testMember2: Member

    @BeforeEach
    fun init() {
        val memberId1 = memberDao.insert(Member(name = "corgi"))
        testMember1 = memberDao.findById(memberId1)

        val memberId2 = memberDao.insert(Member(name = "suri"))
        testMember2 = memberDao.findById(memberId2)
    }

    @DisplayName("기존에 존재하는 방인지 확인한다. :: 기존 방이 없는 경우")
    @Test
    fun isExistDirectRoom() {
        val createdRoom = roomService.create(Room(members = mutableListOf(testMember1, testMember2)))
        val savedRoom = roomService.findDirectRoom(testMember1, testMember2)
        assertThat(createdRoom.id).isEqualTo(savedRoom!!.id)
    }

    @DisplayName("방 입장 :: 멤버가 둘 인 경우")
    @Test
    fun isEnterDirectRoom() {
        val savedRoom = roomService.enter(Room(members = mutableListOf(testMember1, testMember2)))
        assertThat(createdRoom.id).isEqualTo(savedRoom!!.id)
    }

    @DisplayName("기존에 방이 없다면 새로운 방을 생성한다.")
    @Test
    fun createRoomWhenMessageSentAndNotExist() {
        val directMessage = roomService.sendDirectMessage(testMember1, testMember2, "hi")
        assertThat(directMessage.room.id).isNotNull
    }
}