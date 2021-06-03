package com.suco.sucotalk.member.domain

import com.suco.sucotalk.member.exception.MemberException

class Member(val id: Long, val name: String, val password: String = "") {

    init {
        validate()
    }

    constructor(name: String, password: String) : this(0, name, password)

    private fun validate() {
        require(name.isNotBlank()) { throw MemberException("이름은 빈칸일 수 없습니다.") }
        require(name.length < 10) { throw MemberException("이름은 10글자를 넘을 수 없습니다.") }

//        require(password.isNotBlank()) { throw MemberException("비밀번호는 빈칸일 수 없습니다.") }
        require(password.length < 10) { throw MemberException("비밀번호는 10글자를 넘을 수 없습니다.") }
    }

    fun confirmPassword(password: String) {
        check(this.password == password) { throw MemberException("비밀번호가 일치하지 않습니다.") }
    }
}