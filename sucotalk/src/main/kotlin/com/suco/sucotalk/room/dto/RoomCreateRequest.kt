package com.suco.sucotalk.room.dto

import javax.validation.constraints.NotBlank

data class RoomCreateRequest(
    @field:NotBlank(message = "방 이름은 빈칸일 수 없습니다.")
    val name: String,
    val members: List<Long>
)

// 코틀린에서 valid가 동작하지 않는 이유
// https://velog.io/@lsb156/SpringBoot-Kotlin%EC%97%90%EC%84%9C-Valid%EA%B0%80-%EB%8F%99%EC%9E%91%ED%95%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EC%9B%90%EC%9D%B8JSR-303-JSR-380