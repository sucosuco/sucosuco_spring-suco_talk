package com.suco.sucotalk.member.controller

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService) {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
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
    fun login(@RequestBody loginRequest: Member, httpSession: HttpSession): ResponseEntity<Void> {
        val userName = memberService.login(loginRequest)
        httpSession.setAttribute("login-user", userName)
        return ResponseEntity.ok().build()
    }
}