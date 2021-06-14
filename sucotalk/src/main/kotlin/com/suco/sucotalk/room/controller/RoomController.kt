package com.suco.sucotalk.room.controller

import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.room.dto.RoomCreateRequest
import com.suco.sucotalk.room.dto.RoomCreateResponse
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.service.RoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class RoomController(private val roomService: RoomService, private val authService: AuthService) {

    @GetMapping("/rooms")
    fun getAllRoom() : ResponseEntity<List<RoomDto>> {
        return ResponseEntity.ok(roomService.rooms())
    }

    @GetMapping("/rooms/my")
    fun getMyRooms(httpServletRequest: HttpServletRequest) : ResponseEntity<List<RoomDto>> {
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인된 사용자가 아닙니다.")
        return ResponseEntity.ok(roomService.myRooms(userName))
    }

    @GetMapping("/rooms/accessible")
    fun getAccessibleRooms(httpServletRequest: HttpServletRequest) : ResponseEntity<List<RoomDto>>{
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인된 사용자가 아닙니다.")
        return ResponseEntity.ok(roomService.accessibleRooms(userName))
    }

    @GetMapping("/rooms/detail/{room_id}")
    fun getRoomDetail(@PathVariable("room_id") roomId: Long): ResponseEntity<RoomDetail> {
        return ResponseEntity.ok(roomService.roomDetail(roomId));
    }

    @PostMapping("/rooms")
    fun createNewRoom(@RequestBody request : RoomCreateRequest, httpServletRequest: HttpServletRequest): ResponseEntity<RoomCreateResponse>? {
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인된 사용자가 아닙니다.")
        val room :RoomCreateResponse = roomService.createRoom(userName, request)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/rooms/enter/{room_id}")
    fun enterRoom(@PathVariable("room_id") roomId: Long, memberId: Long?, httpServletRequest: HttpServletRequest) {
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인된 사용자가 아닙니다.")
        roomService.enter(userName, roomId)
    }

    @PostMapping("/rooms/exit/{room_id}")
    fun exitRoom(@PathVariable("room_id") roomId: Long, httpServletRequest: HttpServletRequest) {
        val userName = authService.getPayload(httpServletRequest)?:throw IllegalArgumentException("로그인된 사용자가 아닙니다.")
        roomService.exit(userName, roomId)
    }
}