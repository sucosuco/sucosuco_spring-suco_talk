package com.suco.sucotalk.member.service

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.member.repository.MemberDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Transactional(readOnly = true)
@Service
class MemberService(private val memberDao: MemberDao) {

    fun findById(id: Long): Member {
        return memberDao.findById(id)
    }

    @Transactional
    fun createMember(name: String, password: String): Long {
        return memberDao.insert(Member(name, password))
    }

    fun login(loginRequest: Member): MemberDto {
        val savedMember = memberDao.findByName(loginRequest.name)
        savedMember.confirmPassword(savedMember.password)
        return MemberDto.of(savedMember);
    }

    fun findAll(): List<Member>? {
        return memberDao.findAll()
    }

    fun findFriends(userName:String) : List<Member>? {
       return memberDao.findAll().stream()
               .filter {
                   member -> member.name != userName
               }
               .collect(Collectors.toList())
    }
}