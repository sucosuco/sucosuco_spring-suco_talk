package com.suco.sucotalk.room.domain

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.exception.RoomException

const val MINIMUM_MEMBER_SIZE = 2

class Room(val id: Long?, val name: String, members: List<Member> = listOf()) {

    constructor(name: String, members: List<Member>) : this(null, name, members)

    init {
        require(name.isNotBlank()) { throw RoomException("방 이름이 없습니다.") }

        // TODO :: Room, RoomEntity 문제
//        require(members.size >= MINIMUM_MEMBER_SIZE) { throw RoomException("방은 최소 2명 이상이어야 합니다.") }
        require(members.size == members.distinct().size) { throw RoomException("중복된 사용자가 있어선 안됩니다.") }
    }

    val members: MutableList<Member> = members.toMutableList()

    fun enter(member: Member) {
        require(!members.contains(member)) { throw RoomException("이미 방에 존재하는 사용자입니다.") }
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

