package com.suco.sucotalk.room.dto

import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.domain.RoomInfo

data class RoomApproximate(val id: Long?, val name: String) {

    companion object {
        fun of(room: Room): RoomApproximate {
            return RoomApproximate(room.id, room.name)
        }

        fun listOf(rooms: List<RoomInfo>): List<RoomApproximate> {
            return rooms.map { RoomApproximate(it.id, it.name) }
        }
    }
}