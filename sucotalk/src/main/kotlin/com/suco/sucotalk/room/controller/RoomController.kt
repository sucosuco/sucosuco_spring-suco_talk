package com.suco.sucotalk.room.controller

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.dto.RoomCreateRequest
import com.suco.sucotalk.room.dto.RoomCreateResponse
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.service.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpSession

@RestController
class RoomController(private val roomService: RoomService, private val httpSession: HttpSession) {

    @GetMapping("/rooms")
    fun getAllRoom() : ResponseEntity<List<RoomDto>> {
        return ResponseEntity.ok(roomService.rooms())
    }

    @PostMapping("/rooms")
    fun createNewRoom(@RequestBody request : RoomCreateRequest): ResponseEntity<RoomCreateResponse>? {
        val room :RoomCreateResponse = roomService.createRoom(request)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/rooms/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, memberId: Long?) {
        val name = httpSession.getAttribute("login-user") as String
        roomService.enter(name, roomId)
    }

    @PostMapping("/rooms/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, memberId: Long?): Member {
        val name = httpSession.getAttribute("login-user") as String
        return roomService.exit(name, roomId)
    }
}