package com.suco.sucotalk.chat.dto

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.dto.MemberDto

data class MessageDto(private val sender: MemberDto, private val contents: String, private val sendTime: String?) {

    companion object {
       fun of(message: Message): MessageDto {
           return MessageDto(MemberDto.of(message.sender), message.content, message.time)
       }
    }
}