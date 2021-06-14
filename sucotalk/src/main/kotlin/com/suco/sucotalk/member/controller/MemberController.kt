package com.suco.sucotalk.member.controller

import com.suco.sucotalk.auth.dto.TokenResponse
import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService, private val authService: AuthService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<Member>> {
        return ResponseEntity.ok(memberService.findAll())
    }

    @GetMapping("/friends")
    fun findFriends(httpServletRequest: HttpServletRequest): ResponseEntity<List<Member>> {
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인 안된 사용자")
        return ResponseEntity.ok(memberService.findFriends(userName))
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Member> {
        return ResponseEntity.ok(memberService.findById(id))
    }

    @PostMapping
    fun create(@RequestBody memberRequest: Member): ResponseEntity<Void> {
        val createMemberId = memberService.createMember(memberRequest.name, memberRequest.password)
        return ResponseEntity.created(URI.create("/member/$createMemberId")).build()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Member, response:HttpServletResponse): ResponseEntity<MemberDto> {
        val token :String? = authService.login(loginRequest)
        response.addHeader("Authorization", token)
        return ResponseEntity.ok(MemberDto.of(loginRequest))
    }

    @PostMapping("/logout")
    fun logout(httpSession: HttpSession): ResponseEntity<Unit> {
        httpSession.removeAttribute("login-user")
        return ResponseEntity.ok(Unit)
    }
}