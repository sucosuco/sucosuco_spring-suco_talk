package com.suco.sucotalk.room.domain

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.exception.RoomException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RoomTest{

    @DisplayName("방을 생성한다.")
    @Test
    internal fun createRoom() {
        val members = listOf(Member(1L, "corgi", "1234"), Member(2L,"suri", "1234"))
        val room = Room("name", members)
        assertThat(room).isNotNull
    }

    @DisplayName("1명 이하의 사용자로 방 생성 시 예외 발생")
    @Test
    internal fun createRoomWithInsufficientMemberSize() {
        assertThrows<RoomException>{
            Room("name", listOf(Member("ecsimsw", "1234")))
        }
    }

    @DisplayName("중복된 유저가 방에 포함됨")
    @Test
    internal fun createRoomWithDuplicatedMembers() {
        assertThrows<RoomException>{
            val member = Member(1L,"corgi", "1234")
            Room("name", listOf(member, member))
        }
    }

    @DisplayName("방 이름이 없이 생성될 수 없음")
    @Test
    internal fun createRoomWithInvalidName() {
        assertThrows<RoomException>{
            val members = listOf(Member("corgi", "1234"), Member("suri", "1234"))
            Room("", members)
        }
    }
}