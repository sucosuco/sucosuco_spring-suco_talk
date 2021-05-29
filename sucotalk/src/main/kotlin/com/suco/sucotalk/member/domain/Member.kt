package com.suco.sucotalk.member.domain

class Member(val id: Long = 0, val name: String, val password: String) {

    fun validatePassword(password: String) {
        check(this.password == password)
    }
}