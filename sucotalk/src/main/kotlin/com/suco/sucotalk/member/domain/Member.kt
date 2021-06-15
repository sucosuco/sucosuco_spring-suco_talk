package com.suco.sucotalk.member.domain

import com.suco.sucotalk.member.exception.MemberException

const val MAXIMUM_LENGTH_OF_NAME = 10
const val MAXIMUM_LENGTH_OF_PASSWORD = 10

class Member(val id: Long, val name: String, val password: String) {

    init {
        validate()
    }

    constructor(name: String, password: String) : this(0, name, password)

    private fun validate() {
        require(name.isNotBlank()) { throw MemberException("이름은 빈칸일 수 없습니다.") }
        require(name.length < MAXIMUM_LENGTH_OF_NAME) {
            throw MemberException("이름은 $MAXIMUM_LENGTH_OF_NAME 글자를 넘을 수 없습니다.")
        }

        require(password.isNotBlank()) { throw MemberException("비밀번호는 빈칸일 수 없습니다.") }
        require(password.length < MAXIMUM_LENGTH_OF_PASSWORD) {
            throw MemberException("비밀번호는 $MAXIMUM_LENGTH_OF_PASSWORD 글자를 넘을 수 없습니다.")
        }
    }

    fun confirmPassword(password: String) {
        check(this.password == password) {
            throw MemberException("비밀번호가 일치하지 않습니다.")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}