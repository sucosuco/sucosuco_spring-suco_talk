package com.suco.sucotalk.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.service.MemberService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var memberService: MemberService

    val requestMember = Member(name = "test", password = "test")
    lateinit var savedMember: Member

    @BeforeEach
    fun initTestMember() {
        val createdId = memberService.createMember(requestMember.name, requestMember.password)
        savedMember = memberService.findById(createdId)
    }

    @DisplayName("id를 기준으로 member를 탐색한다.")
    @Test
    fun findById() {
        val searchId = savedMember.id
        mockMvc.perform(get("/member/$searchId"))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @DisplayName("member를 생성한다.")
    @Test
    fun createMember() {
        mockMvc.perform(
            post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Member(name = "test2", password = "test2")))
        )
            .andExpect(status().isCreated)
    }

    @DisplayName("로그인 요청을 수행한다. :: 올바른 유저일 경우 세션을 저장")
    @Test
    fun loginMember() {
        val response = mockMvc.perform(
            post("/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMember))
        ).andExpect(status().isOk).andReturn()

        assertThat(response.request.session!!.getAttribute("login-user")).isEqualTo(requestMember.name)
    }

    @DisplayName("로그인 요청을 수행한다. :: 올바르지 않는 유저일 경우 BadRequest 반환")
    @Test
    fun loginMemberWithInvalidUser() {
        val nonExistMember = Member(name = "nonExist", password = "invalid")

        val response = mockMvc.perform(
            post("/member/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nonExistMember))
        ).andExpect(status().isBadRequest).andReturn()

        assertThat(response.request.session!!.getAttribute("login-user")).isNotEqualTo(requestMember.name)
    }
}