package com.suco.sucotalk.room.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RoomRequest(
    @field:NotBlank(message = "방 이름은 빈칸일 수 없습니다.")
    val name: String,

    @field:Size(min = 1, message = "대화할 멤버를 선택해주시길 바랍니다.")
    val members: List<Long>
)