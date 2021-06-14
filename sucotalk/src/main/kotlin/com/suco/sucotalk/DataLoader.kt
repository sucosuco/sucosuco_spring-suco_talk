package com.suco.sucotalk

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.chat.repository.MessageDao
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.service.MemberService
import com.suco.sucotalk.room.domain.Room
import com.suco.sucotalk.room.repository.RoomDao
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class DataLoader(
    private val roomDao: RoomDao,
    private val memberService: MemberService,
    private val messageDao: MessageDao
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val corgiId: Long = memberService.createMember("corgi", "1234")
        val suriId: Long = memberService.createMember("suri", "1234")
        val jinhwanId: Long = memberService.createMember("jinhwan", "1234")
        val dawonId: Long = memberService.createMember("dawon", "1234")

        val corgi: Member = memberService.findById(corgiId)
        val suri: Member = memberService.findById(suriId)
        val jinhwan: Member = memberService.findById(jinhwanId)
        val dawon: Member = memberService.findById(dawonId)

        val room1Id: Long = roomDao.create(Room(name = "room1", members = listOf(corgi, suri)))
        val room2Id: Long = roomDao.create(Room(name = "room2", members = listOf(corgi, suri, jinhwan)))
        val room3Id: Long = roomDao.create(Room(name = "room3", members = listOf(suri, dawon)))

        val room1: Room = roomDao.findById(room1Id)
        val room2: Room = roomDao.findById(room2Id)
        val room3: Room = roomDao.findById(room3Id)

        val message1 = Message(sender = corgi, room = room1, content = "hi")
        val message2 = Message(sender = suri, room = room1, content = "hey")
        val message3 = Message(sender = suri, room = room2, content = "hey")
        val message4 = Message(sender = suri, room = room2, content = "hey")
        val message5 = Message(sender = suri, room = room3, content = "hey")
        val message6 = Message(sender = suri, room = room3, content = "hey")

        messageDao.save(message1)
        messageDao.save(message2)
        messageDao.save(message3)
        messageDao.save(message4)
        messageDao.save(message5)
        messageDao.save(message6)
    }
}
