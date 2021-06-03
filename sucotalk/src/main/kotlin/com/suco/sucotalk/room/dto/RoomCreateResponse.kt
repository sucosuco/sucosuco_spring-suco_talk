package com.suco.sucotalk.room.dto

import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.room.domain.Room

data class RoomCreateResponse(val id: Long, val name: String, val members: List<MemberDto>) {

    companion object {
        fun of(room: Room): RoomCreateResponse {
            return RoomCreateResponse(room.id!!, room.name, MemberDto.listOf(room.members))
        }
    }
}