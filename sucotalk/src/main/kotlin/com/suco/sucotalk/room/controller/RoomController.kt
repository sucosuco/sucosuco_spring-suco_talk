package com.suco.sucotalk.room.controller

import com.suco.sucotalk.auth.domain.Authentication
import com.suco.sucotalk.auth.service.AuthService
import com.suco.sucotalk.member.domain.LoginMember
import com.suco.sucotalk.room.dto.RoomApproximate
import com.suco.sucotalk.room.dto.RoomDetail
import com.suco.sucotalk.room.dto.RoomRequest
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

    @PostMapping("/auth")
    fun createNewRoom(
        @Valid @RequestBody roomInfo: RoomRequest,
        @Authentication loginMember: LoginMember
    ): ResponseEntity<RoomApproximate>? {
        val room = roomService.createRoom(loginMember.name, roomInfo)
        return ResponseEntity.created(URI.create("/rooms/" + room.id)).body(room);
    }

    @PostMapping("/enter/{room_id}/auth")
    fun enterRoom(@PathVariable("room_id") roomId: Long,  @Authentication loginMember: LoginMember) {
        roomService.enter(loginMember.name, roomId)
    }

    @PostMapping("/exit/{room_id}/auth")
    fun exitRoom(@PathVariable("room_id") roomId: Long, @Authentication loginMember: LoginMember) {
        roomService.exit(loginMember.name, roomId)
    }

    @GetMapping("/my/auth")
    fun getMyRooms(@Authentication loginMember: LoginMember): ResponseEntity<List<RoomApproximate>> {
        return ResponseEntity.ok(roomService.myRooms(loginMember.name))
    }

    @GetMapping("/accessible/auth")
    fun getAccessibleRooms(@Authentication loginMember: LoginMember): ResponseEntity<List<RoomApproximate>> {
        return ResponseEntity.ok(roomService.accessibleRooms(loginMember.name))
    }
}