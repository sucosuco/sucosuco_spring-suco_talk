package com.suco.sucotalk.room.dto

import com.suco.sucotalk.room.domain.Room
import java.util.stream.Collectors

data class RoomDto(val id: Long?, val name: String) {

    companion object {
        fun of(room: Room): RoomDto {
            return RoomDto(room.id, room.name)
        }

        fun listOf(rooms: List<Room>): List<RoomDto> {
            return rooms.stream()
                    .map { of(it) }
                    .collect(Collectors.toList())
        }
    }
}