package com.suco.sucotalk.chat.dto

import com.suco.sucotalk.chat.domain.Message
import com.suco.sucotalk.member.dto.MemberResponse
import com.suco.sucotalk.room.dto.RoomApproximate
import java.util.stream.Collectors.toList

data class MessageDto(
    val id: Long?,
    val room: RoomApproximate,
    val sender: MemberResponse,
    val contents: String,
    val sendTime: String?
) {
    companion object {
        fun of(message: Message): MessageDto {
            return MessageDto(
                message.id,
                RoomApproximate.of(message.room),
                MemberResponse.of(message.sender),
                message.content,
                message.time
            )
        }

        fun listOf(messages: List<Message>): List<MessageDto> {
            return messages.stream().map { message -> of(message) }.collect(toList())
        }
    }
}