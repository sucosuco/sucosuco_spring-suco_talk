package com.suco.sucotalk.room.domain

import com.suco.sucotalk.member.domain.Member

class Room(val id: Long? = null, val name: String = "", members: List<Member> = listOf()) {

    val members: MutableList<Member> = members.toMutableList()

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}

