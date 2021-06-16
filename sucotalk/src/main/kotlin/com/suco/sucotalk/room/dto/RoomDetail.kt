package com.suco.sucotalk.room.dto

import com.suco.sucotalk.chat.dto.MessageResponse
import com.suco.sucotalk.room.domain.Room

data class RoomDetail(val room: Room, val messages: List<MessageResponse>) {

    companion object {
        fun of(room: Room, messages: List<MessageResponse>): RoomDetail {
            return RoomDetail(room, messages)
        }
    }
}