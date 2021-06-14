package com.suco.sucotalk.auth.controller

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.servlet.http.HttpServletResponse

@Controller
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Member, response: HttpServletResponse): ResponseEntity<MemberDto> {
        val token: String? = authService.login(loginRequest)
        response.addHeader("Authorization", token)
        return ResponseEntity.ok(MemberDto.of(loginRequest))
    }
}