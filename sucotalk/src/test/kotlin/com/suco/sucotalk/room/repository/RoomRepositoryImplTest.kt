package com.suco.sucotalk.room.repository

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
class RoomRepositoryImplTest {

    @Autowired
    private lateinit var roomRepositoryImpl: RoomRepositoryImpl

    @Autowired
    private lateinit var memberDao: MemberDao

    private lateinit var testMember1: Member
    private lateinit var testMember2: Member

    @BeforeEach
    fun init() {
        val memberId1 = memberDao.insert(Member(name = "test1", "password"))
        testMember1 = memberDao.findById(memberId1)

        val memberId2 = memberDao.insert(Member(name = "test2", "password"))
        testMember2 = memberDao.findById(memberId2)

        roomRepositoryImpl.save(Room(members = mutableListOf(testMember1, testMember2)))
    }

    @DisplayName("유저가 입장해 있는 룸을 찾는다.")
    @Test
    fun findById() {
        val findEnteredRoom = roomRepositoryImpl.findEnteredRooms(testMember1)

        assertThat(findEnteredRoom.size == 1)
        assertThat(findEnteredRoom.first().members.map { it.id })
            .usingRecursiveComparison().isEqualTo(mutableListOf(testMember1.id, testMember2.id))
    }
}