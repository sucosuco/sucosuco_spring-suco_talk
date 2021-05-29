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

    fun save(message: Message): Long {
        val sql = "INSERT INTO MESSAGE (sender_id, room_id, content) VALUES (?, ?, ?)"
        jdbcTemplate.update(sql, message.sender.id, message.room.id, message.content, keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun findByRoom(room: Room): List<Message> {
        val sql = "SELECT MS.id, MS.contents, MS.send_time, ME.id as member_id, ME.name FROM MEMBER as ME JOIN MESSAGE as MS ON ME.id = MS.sender_id WHERE MS.room_id = ?"

        return jdbcTemplate.query(sql, {rs, rn ->
            Message(
                    id = rs.getLong("id"),
                    sender = Member(rs.getLong("member_id"), rs.getString("name")),
                    room = room,
                    content = rs.getString("contents") ,
                    time = rs.getTimestamp("send_time").toString()
            )
        }, room.id)
    }
}