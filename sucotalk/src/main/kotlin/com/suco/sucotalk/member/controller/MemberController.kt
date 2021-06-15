package com.suco.sucotalk.member.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService, private val authService: AuthService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<Member>> {
        return ResponseEntity.ok(memberService.findAll())
    }

    @PostMapping
    fun create(@RequestBody memberRequest: Member): ResponseEntity<Void> {
        val createMemberId = memberService.createMember(memberRequest.name, memberRequest.password)
        return ResponseEntity.created(URI.create("/member/$createMemberId")).build()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Member> {
        return ResponseEntity.ok(memberService.findById(id))
    }

    @GetMapping("/me")
    fun findByToken(request: HttpServletRequest): ResponseEntity<MemberDto> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(memberService.findByName(userName))
    }

    @GetMapping("/friends")
    fun findFriends(request: HttpServletRequest): ResponseEntity<List<Member>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(memberService.findFriends(userName))
    }
}