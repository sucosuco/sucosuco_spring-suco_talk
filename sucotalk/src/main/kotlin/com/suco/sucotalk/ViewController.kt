package com.suco.sucotalk

import com.suco.sucotalk.chat.dto.MessageDto
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.dto.RoomDto
import com.suco.sucotalk.room.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ViewController(private val roomService: RoomService){

    @GetMapping("/rooms/{id}")
    fun test(@PathVariable id :Long, model: Model) : String{
        val room : Room = roomService.findById(id)
        val messages :List<MessageDto> = roomService.messagesInRoom(room)

        model.addAttribute("room", RoomDto.of(room))
        model.addAttribute("messages", messages)
        return "roomdetailTemp"
    }
}