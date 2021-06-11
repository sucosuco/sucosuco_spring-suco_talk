package com.suco.sucotalk.room.controller

import com.suco.sucotalk.room.dto.RoomCreateRequest
import com.suco.sucotalk.room.dto.RoomCreateResponse
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.service.RoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpSession

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class RoomController(private val roomService: RoomService, private val httpSession: HttpSession) {

    @GetMapping("/rooms")
    fun getAllRoom() : ResponseEntity<List<RoomDto>> {
        return ResponseEntity.ok(roomService.rooms())
    }

    @GetMapping("/rooms/my")
    fun getMyRooms() : ResponseEntity<List<RoomDto>> {
        val user = httpSession.getAttribute("login-user") as String
        return ResponseEntity.ok(roomService.myRooms(user))
    }

    @GetMapping("/rooms/accessible")
    fun getAccessibleRooms() : ResponseEntity<List<RoomDto>>{
        val user = httpSession.getAttribute("login-user") as String
        return ResponseEntity.ok(roomService.accessibleRooms(user))
    }

    @GetMapping("/rooms/detail/{room_id}")
    fun getRoomDetail(@PathVariable("room_id") roomId: Long): ResponseEntity<RoomDetail> {
        return ResponseEntity.ok(roomService.roomDetail(roomId));
    }

    @PostMapping("/rooms")
    fun createNewRoom(@RequestBody request : RoomCreateRequest): ResponseEntity<RoomCreateResponse>? {
        val master = httpSession.getAttribute("login-user") as String
        val room :RoomCreateResponse = roomService.createRoom(master, request)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/rooms/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, memberId: Long?) {
        val userName = httpSession.getAttribute("login-user") as String
        roomService.enter(userName, roomId)
    }

    @PostMapping("/rooms/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, httpSession: HttpSession) {
        val userName = httpSession.getAttribute("login-user") as String
        roomService.exit(userName, roomId)
    }
}