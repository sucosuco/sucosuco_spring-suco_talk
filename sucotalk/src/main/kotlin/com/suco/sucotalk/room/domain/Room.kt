package com.suco.sucotalk.room.domain

import com.suco.sucotalk.member.domain.Member

class Room(val id: Long? = null, val name: String = "", members: List<Member> = listOf()) {

    val members : MutableList<Member> = members.toMutableList()

    fun enter(member: Member) {
        members.add(member)
    }

    fun exit(member: Member) {
        members.remove(member)
    }

    fun headCount(): Int {
        return members.size
    }

    fun isDm(): Boolean {
        return members.size == 2
    }
}

