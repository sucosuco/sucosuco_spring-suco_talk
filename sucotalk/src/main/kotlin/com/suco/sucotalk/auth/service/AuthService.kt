package com.suco.sucotalk.auth.service

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.infrastructure.AuthorizationExtractor
import com.suco.sucotalk.auth.infrastructure.JwtTokenProvider
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.exception.MemberException
import com.suco.sucotalk.member.service.MemberService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(private val jwtTokenProvider: JwtTokenProvider, private val memberService: MemberService) {

    fun login(loginMember: MemberRequest): String? {
        try{
            memberService.confirm(loginMember)
            return jwtTokenProvider.createToken(loginMember.name)
        }catch (memberException :MemberException){
            throw AuthException("로그인 실패")
        }catch (e : Exception){
            throw IllegalArgumentException(e.message)
        }
    }

    // TODO :: AuthorizationExtractor.extract(httpServletRequest)
    // 이걸 사용하는게 낫지 않을까??,
    // 나도 httpServletRequest 를 직접 다루는 거에 거부감이 있긴한데 가급적 라이브러리를 사용하는게 어떤지..
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