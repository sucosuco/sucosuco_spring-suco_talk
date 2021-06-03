package com.suco.sucotalk

import com.suco.sucotalk.room.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ViewController(private val roomService: RoomService){

    @GetMapping("/rooms/1")
    fun test(model: Model) : String{

        val tempRoom1 = RoomDTO("Our first room")
        model.addAttribute("room",tempRoom1)

        val tempMsg1 = MessageDTO("코기", "hi")
        val tempMsg2 = MessageDTO("수리", "hi")

        model.addAttribute("messages", listOf(tempMsg1, tempMsg2))
        return "roomdetailTemp"
    }
}

data class RoomDTO(val name :String)

data class MessageDTO(val sender :String, val message:String)