package com.suco.sucotalk.chat.repository

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class MessageDaoTest {

    @Autowired
    lateinit var memberDao:MemberDao

    @Autowired
    lateinit var messageDao: MessageDao

    @Autowired
    lateinit var roomDao: RoomDao

    private lateinit var testMessage :Message
    private lateinit var testMember1 :Member
    private lateinit var testMember2 :Member
    private lateinit var testRoom : Room

    @BeforeEach
    fun init(){
        val memberId1 = memberDao.insert(Member(name = "corgi"))
        testMember1 = memberDao.findById(memberId1)

        val memberId2 = memberDao.insert(Member(name = "suri"))
        testMember2 = memberDao.findById(memberId2)

        val roomId = roomDao.create(Room(members = mutableListOf(testMember1, testMember2)))
        testRoom = roomDao.findById(roomId)

        roomDao.saveParticipants(testRoom)
    }

    @Test
    fun save() {
        val savedId1 = messageDao.save(Message(sender = testMember1, room = testRoom, content = "hi"))
        assertThat(savedId1).isNotNull
    }

    @Test
    fun findMessagesInRoom() {
        val savedId1 = messageDao.save(Message(sender = testMember1, room = testRoom, content = "hi"))
        val savedId2 = messageDao.save(Message(sender = testMember2, room = testRoom, content = "hey"))
        val savedId3 = messageDao.save(Message(sender = testMember1, room = testRoom, content = "bye"))
        val messagesInRoom : List<Message> = messageDao.findByRoom(testRoom)

        assertThat(messagesInRoom).hasSize(3)
        assertThat(messagesInRoom.map { it.id }).usingRecursiveComparison().isEqualTo(mutableListOf(savedId1, savedId2, savedId3))
    }
}