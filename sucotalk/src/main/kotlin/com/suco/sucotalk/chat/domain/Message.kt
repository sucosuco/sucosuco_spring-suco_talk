package com.suco.sucotalk.chat.domain

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room

data class Message (
    val id: Long?, val sender: Member, val room: Room,
    val content: String, val time: String?
){
    constructor(sender: Member, room: Room, content: String) : this(null, sender, room, content, null)
}