package com.suco.sucotalk.member.controller

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.service.MemberService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/member")
class MemberController(private val memberService: MemberService ) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<Member> {
        return ResponseEntity.ok(memberService.findById(id))
    }

    @PostMapping
    fun create(@RequestBody memberRequest: Member) : ResponseEntity<Void> {
        val createMemberId = memberService.createMember(memberRequest.name, memberRequest.password)
        return ResponseEntity.created(URI.create("/member/$createMemberId")).build()
    }
}