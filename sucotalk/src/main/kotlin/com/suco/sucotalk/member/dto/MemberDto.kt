package com.suco.sucotalk.member.dto

import com.suco.sucotalk.member.domain.Member

data class MemberDto(val id: Long, val name: String) {

    companion object {

        fun of(id: Long, name: String): MemberDto {
            return MemberDto(id, name)
        }

        fun of(member: Member): MemberDto {
            return MemberDto(member.id, member.name)
        }
    }
}