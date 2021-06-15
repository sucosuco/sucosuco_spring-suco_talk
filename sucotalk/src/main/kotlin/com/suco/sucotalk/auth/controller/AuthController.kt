package com.suco.sucotalk.auth.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.servlet.http.HttpServletResponse

@Controller
class AuthController(private val authService: AuthService, private val memberService: MemberService) {

    @PostMapping("/login")
    fun login(@RequestBody request: MemberRequest, response: HttpServletResponse): ResponseEntity<MemberResponse> {
        val token: String? = authService.login(request)
        response.addHeader("Authorization", token)

        return ResponseEntity.ok(memberService.findByName(request.name))
    }
}