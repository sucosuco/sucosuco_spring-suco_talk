package com.suco.sucotalk.auth.service

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.infrastructure.AuthorizationExtractor
import com.suco.sucotalk.auth.infrastructure.JwtTokenProvider
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.exception.MemberException
import com.suco.sucotalk.member.service.MemberService
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(private val jwtTokenProvider: JwtTokenProvider, private val memberService: MemberService) {

    fun login(loginMember: MemberRequest): String? {
        try {
            memberService.confirm(loginMember)
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

    fun getPayload(httpServletRequest: HttpServletRequest): String {
        try {
            val token = AuthorizationExtractor.extract(httpServletRequest)
            return jwtTokenProvider.getPayload(token)
        } catch (e: Exception) {
            throw AuthException("로그인된 사용자가 아닙니다.")
        }
    }
}