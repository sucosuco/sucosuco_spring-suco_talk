package com.suco.sucotalk.member.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class MemberRequest(
    @field:NotBlank(message = "이름은 빈칸일 수 없습니다.")
    @field:Length(max = 10, message = "이름은 10글자를 넘을 수 없습니다.")
    val name: String,

    @field:NotBlank(message = "비밀번호는 빈칸일 수 없습니다.")
    @field:Length(max = 10, message = "비밀번호는 10글자를 넘을 수 없습니다.")
    val password: String
)