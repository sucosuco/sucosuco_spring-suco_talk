package com.suco.sucotalk.room.dto

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room

data class RoomApproximate(val id: Long?, val name: String, val members: List<Member>) {

    companion object {
        fun of(room: Room): RoomApproximate {
            return RoomApproximate(room.id, room.name, room.members)
        }

        fun listOf(rooms: List<Room>): List<RoomApproximate> {
            return rooms.map { of(it) }
        }
    }
}