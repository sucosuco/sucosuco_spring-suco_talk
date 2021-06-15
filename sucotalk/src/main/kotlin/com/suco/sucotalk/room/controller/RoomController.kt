package com.suco.sucotalk.room.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.room.dto.RoomCreateRequest
import com.suco.sucotalk.room.dto.RoomCreateResponse
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.service.RoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

// TODO :: CORS 설정 방식
@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class RoomController(private val roomService: RoomService, private val authService: AuthService) {

    @GetMapping("/rooms")
    fun getAllRoom(): ResponseEntity<List<RoomDto>> {
        return ResponseEntity.ok(roomService.rooms())
    }

    @GetMapping("/rooms/detail/{room_id}")
    fun getRoomDetail(@PathVariable("room_id") roomId: Long): ResponseEntity<RoomDetail> {
        return ResponseEntity.ok(roomService.roomDetail(roomId));
    }

    @PostMapping("/rooms")
    fun createNewRoom(@RequestBody roomInfo: RoomCreateRequest, request: HttpServletRequest): ResponseEntity<RoomCreateResponse>? {
        val userName = authService.getPayload(request)
        val room: RoomCreateResponse = roomService.createRoom(userName, roomInfo)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/rooms/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, request: HttpServletRequest) {
        val userName = authService.getPayload(request)
        roomService.enter(userName, roomId)
    }

    @PostMapping("/rooms/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, request: HttpServletRequest) {
        val userName = authService.getPayload(request)
        roomService.exit(userName, roomId)
    }

    @GetMapping("/rooms/my")
    fun getMyRooms(request: HttpServletRequest): ResponseEntity<List<RoomDto>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(roomService.myRooms(userName))
    }

    @GetMapping("/rooms/accessible")
    fun getAccessibleRooms(request: HttpServletRequest): ResponseEntity<List<RoomDto>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(roomService.accessibleRooms(userName))
    }
}