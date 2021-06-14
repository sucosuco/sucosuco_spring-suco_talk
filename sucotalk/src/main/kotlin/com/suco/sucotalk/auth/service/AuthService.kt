package com.suco.sucotalk.auth.service

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.infrastructure.AuthorizationExtractor
import com.suco.sucotalk.auth.infrastructure.JwtTokenProvider
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.member.service.MemberService
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(private val jwtTokenProvider: JwtTokenProvider, private val memberService: MemberService) {

    fun login(loginMember: Member): String? {
        val member: MemberDto = memberService.login(loginMember)
        val payload: String = member.name
        return jwtTokenProvider.createToken(payload)
    }

    fun getPayload(httpServletRequest: HttpServletRequest): String {
        try {
            val token = AuthorizationExtractor.extract(httpServletRequest)
            return jwtTokenProvider.getPayload(token)
        } catch (e: Exception) {
            throw AuthException("로그인된 사용자가 아닙니다.")
        }
    }
}