package com.suco.sucotalk.member.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.suco.sucotalk.DataLoader
import com.suco.sucotalk.member.domain.Member
import com.suco.sucotalk.member.dto.MemberRequest
import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.member.service.MemberService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
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

    @MockBean
    lateinit var dataLoader: DataLoader

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var memberService: MemberService

    val requestMember = MemberRequest( "test", "test")
    lateinit var savedMember: MemberResponse

    @BeforeEach
    fun initTestMember() {
        val createdId = memberService.createMember(requestMember)
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
                .content(objectMapper.writeValueAsString(MemberRequest( "test2",  "test2")))
        ).andExpect(status().isCreated)
    }

    @DisplayName("로그인 요청을 수행한다. :: 올바른 유저일 경우 토큰을 반환")
    @Test
    fun loginMember() {
        val response = requestLogin(requestMember).andExpect(status().isOk).andReturn().response
        assertThat(response.getHeaderValue("Authorization")).isNotNull
    }

    @DisplayName("로그인 요청을 수행한다. :: 올바르지 않는 유저일 경우 BadRequest 반환")
    @Test
    fun loginMemberWithInvalidUser() {
        val nonExistMember = MemberRequest( "nonExist",  "invalid")
        requestLogin(nonExistMember).andExpect(status().isBadRequest)
    }

    @DisplayName("유저 정보가 필요한 요청을 수행한다 :: 로그인 토큰이 있는 경우")
    @Test
    fun checkUserToken() {
        val response = requestLogin(requestMember).andReturn().response
        val userToken: String = response.getHeaderValue("Authorization").toString()

        mockMvc.perform(
            get("/rooms/my")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $userToken")
        )
            .andExpect(status().isOk)
    }

    private fun requestLogin(requestMember: MemberRequest) = mockMvc.perform(
        post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestMember))
    )
}