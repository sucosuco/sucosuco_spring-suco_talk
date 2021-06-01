package com.suco.sucotalk.room.controller

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class RoomController(private val roomService: RoomService) {

    @PostMapping("/rooms")
    fun createNewRoom(members: List<Long>, name: String = "") {
        roomService.enterNewRoom(members)
    }

    @PostMapping("/rooms/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, memberId: Long) {
        roomService.enter(memberId, roomId)
    }

    @PostMapping("/rooms/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, memberId: Long): Member {
        val exitMember = roomService.exit(memberId, roomId)
        return exitMember
    }
}