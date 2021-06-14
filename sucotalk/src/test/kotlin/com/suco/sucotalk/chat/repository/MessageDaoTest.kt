package com.suco.sucotalk.chat.repository

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class MessageDaoTest {

    @Autowired
    lateinit var memberDao: MemberDao

    @Autowired
    lateinit var messageDao: MessageDao

    @Autowired
    lateinit var roomDao: RoomDao

    private lateinit var testMember1: Member
    private lateinit var testMember2: Member
    private lateinit var testRoom: Room

    @BeforeEach
    fun init() {
        val memberId1 = memberDao.insert(Member("test1", "password"))
        testMember1 = memberDao.findById(memberId1)

        val memberId2 = memberDao.insert(Member("test2", "password"))
        testMember2 = memberDao.findById(memberId2)

        val roomId = roomDao.create(Room("testRoom", listOf(testMember1, testMember2)))
        testRoom = roomDao.findById(roomId)

        roomDao.saveParticipants(testRoom)
    }

    @Test
    fun save() {
        val savedId = messageDao.save(Message(sender = testMember1, room = testRoom, content = "hi"))
        assertThat(savedId).isNotNull
    }

    @Test
    fun findByRoom() {
        val savedMessage1 = messageDao.save(Message(sender = testMember1, room = testRoom, content = "hi"))
        val savedMessage2 = messageDao.save(Message(sender = testMember2, room = testRoom, content = "hey"))
        val savedMessage3 = messageDao.save(Message(sender = testMember1, room = testRoom, content = "bye"))
        val messagesInRoom: List<Message> = messageDao.findByRoom(testRoom)

        assertThat(messagesInRoom).hasSize(3)
        assertThat(messagesInRoom.map { it.id }).usingRecursiveComparison()
            .isEqualTo(mutableListOf(savedMessage1.id, savedMessage2.id, savedMessage3.id))
    }
}