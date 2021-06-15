package com.suco.sucotalk.member.dto

import com.suco.sucotalk.member.domain.Member

data class MemberResponse(val id: Long, val name: String) {

    companion object {

        fun of(id:Long, name: String): MemberResponse {
            return MemberResponse(id, name)
        }

        fun of(member: Member): MemberResponse {
            return MemberResponse(member.id, member.name)
        }

        fun listOf(members: List<Member>): List<MemberResponse> {
            return members.map { of(it) }
        }
    }
}