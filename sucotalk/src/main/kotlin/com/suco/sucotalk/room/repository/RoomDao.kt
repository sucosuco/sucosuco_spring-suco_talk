package com.suco.sucotalk.room.repository

import com.suco.sucotalk.room.domain.Room
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class RoomDao(private val jdbcTemplate: JdbcTemplate) {

    private val keyHolder = GeneratedKeyHolder()

    fun create(room: Room): Long {
        val sql = "INSERT ROOM (name) VALUES (?) "

        jdbcTemplate.update({
            val ps = it.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, room.name)
            ps
        }, keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun findById(id: Long): Room {
        val sql = "SELECT * FROM ROOM WHERE id = ?"

        try {
            return jdbcTemplate.queryForObject(sql, { rs, rn ->
                Room(rs.getLong("id"), rs.getString("name"))
            }, id)!!
        } catch (e: EmptyResultDataAccessException) {
            throw IllegalArgumentException("등록되지 않은 아이디 입니다.")
        }
    }

    fun findParticipantsById(id: Long): List<Long> {
        val sql = "SELECT member_id FROM PARTICIPANTS WHERE room_id = ?"
        return jdbcTemplate.query(sql) { rs, rn ->
            rs.getLong("member_id")
        }
    }
}