package com.suco.sucotalk.room.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.room.domain.Room
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class RoomDao(private val jdbcTemplate: JdbcTemplate) {

    private val keyHolder = GeneratedKeyHolder()

    fun getAllRoom(): List<Room> {
        val sql = "SELECT * FROM ROOM"

        return jdbcTemplate.query(sql) { rs, rn ->
            Room(rs.getLong("id"), rs.getString("name"))
        }
    }

    fun create(room: Room): Long {
        val sql = "INSERT INTO ROOM (name) VALUES (?) "

        jdbcTemplate.update({
            val ps = it.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, room.name)
            ps
        }, keyHolder)
        saveParticipants(Room(keyHolder.key!!.toLong(), room.name, room.members))
        return keyHolder.key!!.toLong()
    }

    fun saveParticipants(room: Room) {
        val sql = "INSERT INTO PARTICIPANTS (member_id, room_id) VALUES(?, ?)"
        jdbcTemplate.batchUpdate(sql, room.members, 100) { ps, argument ->
            ps.setLong(1, argument.id)
            ps.setLong(2, room.id!!)
        }
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
        return jdbcTemplate.query(sql, { rs, rn ->
            rs.getLong("member_id")
        }, id)
    }

    fun findRoomByMember(member: Member): List<Long> {
        val sql = "SELECT room_id FROM PARTICIPANTS WHERE member_id = ?"
        return jdbcTemplate.query(sql, { rs, rn ->
            rs.getLong("room_id")
        }, member.id)
    }

    fun deleteMemberInRoom(room: Room, member: Member) {
        val sql = "DELETE FROM PARTICIPANTS WHERE room_id = ? AND member_id = ?"
        jdbcTemplate.update(sql, room.id, member.id)
    }

    fun insertMemberInRoom(room: Room, member: Member) {
        val sql = "INSERT INTO PARTICIPANTS (member_id, room_id) VALUES (?, ?)"
        jdbcTemplate.update(sql, member.id, room.id)
    }
}