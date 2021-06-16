package com.suco.sucotalk.member.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService, private val authService: AuthService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<MemberResponse>> {
        return ResponseEntity.ok(memberService.findAll())
    }

    @PostMapping
    fun create(@Valid @RequestBody memberRequest: MemberRequest): ResponseEntity<Void> {
        val createMemberId = memberService.createMember(memberRequest)
        return ResponseEntity.created(URI.create("/member/$createMemberId")).build()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.findById(id))
    }

    @GetMapping("/me")
    fun findByToken(request: HttpServletRequest): ResponseEntity<MemberResponse> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(memberService.findByName(userName))
    }

    @GetMapping("/friends")
    fun findFriends(request: HttpServletRequest): ResponseEntity<List<MemberResponse>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(memberService.findFriends(userName))
    }
}