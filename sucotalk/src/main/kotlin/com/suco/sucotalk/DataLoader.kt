package com.suco.sucotalk

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.repository.MemberDao
import com.suco.sucotalk.member.service.MemberService
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomDao
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataLoader(private val roomDao: RoomDao, private val memberService: MemberService, private val messageDao: MessageDao) : CommandLineRunner {

    override fun run(vararg args: String) {
        val corgiId :Long = memberService.createMember("corgi", "1234")
        val suriId :Long = memberService.createMember("suri", "1234")

        val corgi : Member = memberService.findById(corgiId)
        val suri :Member = memberService.findById(suriId)

        val roomId :Long = roomDao.create(Room(name = "test", members = listOf(corgi, suri)))
        val room :Room = roomDao.findById(roomId)

        val message1 = Message(sender = corgi, room = room, content = "hi")
        val message2 = Message(sender = suri, room = room, content = "hey")

        messageDao.save(message1)
        messageDao.save(message2)
    }
}
