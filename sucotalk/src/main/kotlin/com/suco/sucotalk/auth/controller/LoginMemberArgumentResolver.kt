package com.suco.sucotalk.auth.controller

import com.suco.sucotalk.auth.domain.Authentication
import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.infrastructure.AuthorizationExtractor
import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.domain.LoginMember
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

@Component
class LoginMemberArgumentResolver(private val authService: AuthService): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Authentication::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {

        val request = webRequest.nativeRequest as HttpServletRequest
        val token = AuthorizationExtractor.extract(request)

        token?.let {
            return LoginMember.from(authService.findMemberByToken(it))
        }

        throw AuthException("유효하지 않은 토큰입니다.")
    }
}