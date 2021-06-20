package com.suco.sucotalk.auth.infrastructure
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginInterceptor(private val jwtTokenProvider: JwtTokenProvider): HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessToken = AuthorizationExtractor.extract(request)
        jwtTokenProvider.validateToken(accessToken)
        return true;
    }
}