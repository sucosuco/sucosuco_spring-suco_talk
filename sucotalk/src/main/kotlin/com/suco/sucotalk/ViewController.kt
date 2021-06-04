package com.suco.sucotalk

import com.suco.sucotalk.room.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ViewController(private val roomService: RoomService) {

    @GetMapping("/rooms/{id}")
    fun showRoom(@PathVariable id: Long, model: Model): String {
        val roomDetail = roomService.roomDetail(id)

        model.addAttribute("room", roomDetail.room)
        model.addAttribute("messages", roomDetail.messages)

        return "roomdetail"
    }
}