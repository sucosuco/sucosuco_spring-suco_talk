package com.suco.sucotalk.member.dto

import com.suco.sucotalk.member.domain.Member

data class MemberDto(val id: Long, val name: String) {

    companion object {
        fun of(member: Member): MemberDto {
            return MemberDto(member.id, member.name)
        }

        fun listOf(members: MutableList<Member>): List<MemberDto> {
            return members.map { of(it) }
        }
    }
}