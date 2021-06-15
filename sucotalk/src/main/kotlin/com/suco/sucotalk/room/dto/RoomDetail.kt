package com.suco.sucotalk.room.dto

import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.room.domain.Room

data class RoomDetail(val room: RoomInformation, val messages: List<MessageDto>) {

    companion object {
        fun of(room: Room, messages: List<MessageDto>): RoomDetail {
            return RoomDetail(RoomInformation.of(room), messages)
        }
    }
}