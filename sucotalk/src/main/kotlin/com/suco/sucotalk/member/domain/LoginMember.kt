package com.suco.sucotalk.member.domain

class LoginMember (val id: Long, val name: String) {

    companion object {
        fun from(member: Member) : LoginMember {
            return LoginMember(member.id, member.name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginMember

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}