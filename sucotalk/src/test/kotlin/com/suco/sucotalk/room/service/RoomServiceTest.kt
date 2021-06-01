package com.suco.sucotalk.room.service

import com.suco.sucotalk.member.service.MemberService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class RoomServiceTest {

    @Autowired
    lateinit var roomService: RoomService

    @Autowired
    lateinit var memberService: MemberService

    /*@Test
    fun exit() {
        roomService.enter()
    }

    @Test
    fun enter() {
    }*/
}