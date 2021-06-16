package com.suco.sucotalk.member.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.member.repository.MemberDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class MemberService(private val memberDao: MemberDao) {

    fun findById(id: Long): MemberResponse {
        return MemberResponse.of(memberDao.findById(id))
    }

    @Transactional
    fun createMember(request: MemberRequest): Long {
        return memberDao.insert(Member(request.name, request.password))
    }

    fun confirm(request: MemberRequest) {
        val user = memberDao.findByName(request.name)
        user.confirmPassword(request.password)
    }

    fun findAll(): List<MemberResponse> {
        return MemberResponse.listOf(memberDao.findAll())
    }

    fun findFriends(userName: String): List<MemberResponse>? {
        val friends: List<Member> = memberDao.findAll().filter { it.name != userName }
        return MemberResponse.listOf(friends)
    }

    fun findByName(userName: String): MemberResponse {
        return MemberResponse.of(memberDao.findByName(userName))
    }
}