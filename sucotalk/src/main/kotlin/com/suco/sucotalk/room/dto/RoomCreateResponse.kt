package com.suco.sucotalk.room.dto

import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.room.domain.Room

data class RoomCreateResponse(val id: Long, val name: String, val members: List<MemberResponse>) {

    companion object {
        fun of(room: Room): RoomCreateResponse {
            return RoomCreateResponse(room.id!!, room.name, MemberResponse.listOf(room.members))
        }
    }
}