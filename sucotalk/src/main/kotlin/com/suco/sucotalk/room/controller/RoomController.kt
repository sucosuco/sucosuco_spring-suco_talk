package com.suco.sucotalk.room.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.room.dto.RoomRequest
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomApproximate
import com.suco.sucotalk.room.service.RoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RequestMapping("/rooms")
@RestController
class RoomController(private val roomService: RoomService, private val authService: AuthService) {

    @GetMapping
    fun getAllRoom(): ResponseEntity<List<RoomApproximate>> {
        return ResponseEntity.ok(roomService.rooms())
    }

    @GetMapping("/detail/{room_id}")
    fun getRoomDetail(@PathVariable("room_id") roomId: Long): ResponseEntity<RoomDetail> {
        return ResponseEntity.ok(roomService.roomDetail(roomId));
    }

    @PostMapping
    fun createNewRoom(@Valid @RequestBody roomInfo: RoomRequest, request: HttpServletRequest): ResponseEntity<RoomApproximate>? {
        val userName = authService.getPayload(request)
        val room: RoomApproximate = roomService.createRoom(userName, roomInfo)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, request: HttpServletRequest) {
        val userName = authService.getPayload(request)
        roomService.enter(userName, roomId)
    }

    @PostMapping("/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, request: HttpServletRequest) {
        val userName = authService.getPayload(request)
        roomService.exit(userName, roomId)
    }

    @GetMapping("/my")
    fun getMyRooms(request: HttpServletRequest): ResponseEntity<List<RoomApproximate>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(roomService.myRooms(userName))
    }

    @GetMapping("/accessible")
    fun getAccessibleRooms(request: HttpServletRequest): ResponseEntity<List<RoomApproximate>> {
        val userName = authService.getPayload(request)
        return ResponseEntity.ok(roomService.accessibleRooms(userName))
    }
}