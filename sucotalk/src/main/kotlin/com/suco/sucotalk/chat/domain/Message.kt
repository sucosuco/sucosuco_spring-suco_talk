package com.suco.sucotalk.chat.domain

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room

class Message(
    val id: Long? = null, val sender: Member, val room: Room,
    val content: String, val time: String? = null
) {

}