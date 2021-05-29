package com.suco.sucotalk.member.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberDao: MemberDao) {
    fun findById(id: Long): Member {
        return memberDao.findById(id)
    }

    fun createMember(name: String, password: String): Long {
        return memberDao.insert(Member(name = name, password = password))
    }

    fun login(loginRequest: Member): String {
        val savedMember = memberDao.findByName(loginRequest.name)
        savedMember.validatePassword(savedMember.password)
        return savedMember.name
    }
}