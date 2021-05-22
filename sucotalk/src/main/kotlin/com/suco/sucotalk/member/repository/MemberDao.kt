package com.suco.sucotalk.member.repository

import com.suco.sucotalk.member.domain.Member
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

@Repository
class MemberDao(private val jdbcTemplate: JdbcTemplate) {

    private val keyHolder = GeneratedKeyHolder()

    fun insert(member: Member): Long {
        val sql = "INSERT INTO MEMBER (name, password) VALUES (?,?)";

        jdbcTemplate.update({
            val ps = it.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, member.name)
            ps.setString(2, member.password)
            ps
        }, keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun findById(id: Long) : Member {
        val sql = "SELECT * FROM MEMBER WHERE id = ?"
        return jdbcTemplate.queryForObject(sql, rowMapper, id)?:
            throw IllegalArgumentException("")
    }

    fun findByName(name: String) : Member {
        val sql = "SELECT * FROM MEMBER WHERE name = ?"
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, name) ?:
                throw IllegalArgumentException("")
        } catch (e : EmptyResultDataAccessException) {
            throw IllegalArgumentException("등록되지 않은 아이디 입니다.")
        }
    }

    private val rowMapper = RowMapper<Member> { rs, rn ->
        Member(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("password"))
    }
}