package com.suco.sucotalk.auth.service

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.infrastructure.AuthorizationExtractor
import com.suco.sucotalk.auth.infrastructure.JwtTokenProvider
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.exception.MemberException
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.member.service.MemberService
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(private val jwtTokenProvider: JwtTokenProvider, private val memberDao: MemberDao) {

    fun login(loginMember: MemberRequest): String? {
        try {
            val user = memberDao.findByName(loginMember.name)
            user.confirmPassword(loginMember.password)
            return jwtTokenProvider.createToken(loginMember.name)
        } catch (memberException: MemberException) {
            throw AuthException("로그인 실패")
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }

    fun getPayloadFromBearer(bearerToken: String): String {
        try {
            val token = bearerToken.split(" ")[1]
            return jwtTokenProvider.getPayload(token)
        } catch (e: Exception) {
            throw AuthException("로그인된 사용자가 아닙니다.")
        }
    }

    fun findMemberByToken(token: String): Member {
        val memberName = jwtTokenProvider.getPayload(token)
        return memberDao.findByName(memberName)
    }
}