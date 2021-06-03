package com.suco.sucotalk.member.domain

import com.suco.sucotalk.member.exception.MemberException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class MemberTest {

    @DisplayName("유효한 아이디와 비밀번호로 회원 생성")
    @Test
    fun createMember() {
        assertThat(Member("valid", "valid")).isNotNull
    }

    @DisplayName("아이디는 빈칸 또는 10글자를 넘을 수 없다.")
    @Test
    fun createMemberWithInvalidName() {
        assertThrows<MemberException> { Member("", "password") }
        assertThrows<MemberException> { Member("12345678901", "password") }
    }

    @DisplayName("비밀번호는 10글자를 넘을 수 없다.")
    @Test
    fun createMemberWithInvalidPassword() {
        assertThrows<MemberException> { Member("valid", "tooLongPasword") }
    }

    @DisplayName("비밀번호 일치 여부하지 않으면 예외를 발생한다.")
    @Test
    fun confirmPassword() {
        val member = Member("email", "password")
        assertThrows<MemberException> { member.confirmPassword("notCorrect") }
    }
}