package com.suco.sucotalk.chat.repository

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class MessageDao(private val jdbcTemplate: JdbcTemplate) {

    private val keyHolder = GeneratedKeyHolder()

    fun save(message: Message): Message {
        val sql = "INSERT INTO MESSAGE (sender_id, room_id, contents) VALUES (?, ?, ?)"
        jdbcTemplate.update({
            val ps = it.prepareStatement(sql, arrayOf("id"))
            ps.setLong(1, message.sender.id)
            ps.setLong(2, message.room.id!!)
            ps.setString(3, message.content)
            ps
        }, keyHolder)
        val id = keyHolder.key!!.toLong()
        return Message(id,message.sender, message.room, message.content)
    }

    fun findByRoom(room: Room): List<Message> {
        val sql = "SELECT MS.id, MS.contents, MS.send_time, ME.id as member_id, ME.name FROM MEMBER as ME JOIN MESSAGE as MS ON ME.id = MS.sender_id WHERE MS.room_id = ?"

        return jdbcTemplate.query(sql, { rs, rn ->
            Message(
                id = rs.getLong("id"),
                sender = Member(rs.getLong("member_id"), rs.getString("name")),
                room = room,
                content = rs.getString("contents"),
                time = rs.getTimestamp("send_time").toString()
            )
        }, room.id)
    }
}