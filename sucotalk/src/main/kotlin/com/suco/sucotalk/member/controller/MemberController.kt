package com.suco.sucotalk.member.controller

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberDto
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpSession

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService, private val httpSession: HttpSession) {

    @GetMapping
    fun findAll(): ResponseEntity<List<Member>> {
        return ResponseEntity.ok(memberService.findAll())
    }

    @GetMapping("/friends")
    fun findFriends(): ResponseEntity<List<Member>> {
        val name = httpSession.getAttribute("login-user") as String
        return ResponseEntity.ok(memberService.findFriends(name))
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
    fun login(@RequestBody loginRequest: Member, httpSession: HttpSession): ResponseEntity<MemberDto> {
        val memberDto = memberService.login(loginRequest)
        httpSession.setAttribute("login-user", memberDto.name)
        return ResponseEntity.ok(memberDto)
    }

    @PostMapping("/logout")
    fun login(httpSession: HttpSession): ResponseEntity<Unit> {
        httpSession.removeAttribute("login-user")
        return ResponseEntity.ok(Unit)
    }
}