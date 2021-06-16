package com.suco.sucotalk.chat.repository

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return Message(id, message.sender, message.room, message.content, now)
    }
    // TODO :: 시간 처리

    // TODO :: join 문 복습
    fun findByRoom(room: Room): List<Message> {
        val sql = "SELECT MS.id, MS.contents, MS.send_time, ME.id as member_id, ME.name, ME.password FROM MEMBER as ME JOIN MESSAGE as MS ON ME.id = MS.sender_id WHERE MS.room_id = ?"
        return jdbcTemplate.query(sql, rowMapper(room), room.id)
    }

    private fun rowMapper(room:Room) = RowMapper<Message> { rs, rn ->
        Message(
            id = rs.getLong("id"),
            sender = Member(rs.getLong("member_id"), rs.getString("name"), rs.getString("password")),
            room = room,
            content = rs.getString("contents"),
            time = rs.getTimestamp("send_time").toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
    }
}