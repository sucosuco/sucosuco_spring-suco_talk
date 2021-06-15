package com.suco.sucotalk.room.dto

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room

data class RoomInformation(val id: Long?, val name: String, val members: List<Member>) {

    companion object {
        fun of(room: Room): RoomInformation {
            return RoomInformation(room.id, room.name, room.members)
        }

        fun listOf(rooms: List<Room>): List<RoomInformation> {
            return rooms.map { of(it) }
        }
    }
}