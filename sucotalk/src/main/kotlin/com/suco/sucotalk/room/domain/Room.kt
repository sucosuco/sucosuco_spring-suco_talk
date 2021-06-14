package com.suco.sucotalk.room.domain

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.exception.RoomException

class Room(val id: Long? = null, val name: String = "", members: List<Member> = listOf()) {

    val members: MutableList<Member> = members.toMutableList()

    fun enter(member: Member) {
        members.add(member)
    }

    fun exit(member: Member) {
        require(members.contains(member)) { throw RoomException("방에 존재하지 않는 사용자입니다.") }
        members.remove(member)
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

