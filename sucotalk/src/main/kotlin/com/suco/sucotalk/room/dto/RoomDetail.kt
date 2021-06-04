package com.suco.sucotalk.room.dto

import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.room.domain.Room

data class RoomDetail(val room:RoomDto, val messages:List<MessageDto>){
    constructor(room: Room, messages:List<MessageDto>) : this(RoomDto.of(room), messages)
}