package com.suco.sucotalk.member.repository

import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.exception.MemberException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.SQLException

@Repository
class MemberDao(
    private val jdbcTemplate: JdbcTemplate,
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

    private val keyHolder = GeneratedKeyHolder()

    fun insert(name:String, password:String): Long {
        val sql = "INSERT INTO MEMBER (name, password) VALUES (?,?)";

        jdbcTemplate.update({
            val ps = it.prepareStatement(sql, arrayOf("id"))
            ps.setString(1, name)
            ps.setString(2, password)
            ps
        }, keyHolder)
        return keyHolder.key!!.toLong()
    }

    fun insert(member: Member): Long {
        return insert(member.name, member.password)
    }

    fun findAll(): List<Member> {
        val sql = "SELECT * FROM MEMBER"
        return jdbcTemplate.query(sql, rowMapper)
    }

    fun findById(id: Long): Member {
        try {
            val sql = "SELECT * FROM MEMBER WHERE id = ?"
            return jdbcTemplate.queryForObject(sql, rowMapper, id)!!
        } catch (emptyResultException : EmptyResultDataAccessException) {
            throw MemberException("존재하지 않는 회원입니다.")
        } catch (e : Exception){
            throw SQLException("error with jdbcTemplate")
        }
    }

    fun findByName(name: String): Member {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM MEMBER WHERE name = ?", rowMapper, name)!!
        } catch (emptyResultException : EmptyResultDataAccessException) {
            throw MemberException("존재하지 않는 회원입니다.")
        } catch (e : Exception){
            throw SQLException("error with jdbcTemplate")
        }
    }

    fun findByIds(participants: List<Long>): List<Member> {
        val parameters = MapSqlParameterSource("ids", participants)
        val sql = "SELECT * FROM MEMBER WHERE id IN (:ids)"

        return namedParameterJdbcTemplate.query(sql, parameters, rowMapper)
    }

    private val rowMapper = RowMapper<Member> { rs, rn ->
        Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("password")
        )
    }
}