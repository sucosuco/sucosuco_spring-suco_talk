package com.suco.sucotalk.chat.dto

import java.time.LocalDateTime

//data class SendMessageDto(val roomId: Long, val senderName: String?,
//                          val contents: String, val sendTime: String = LocalDateTime.now().toString()) {

data class SendMessageDto(val roomId: Long, val senderName: String?,
                              val contents: String) {
        //TODO :: tostring 제거
//    override fun toString(): String {
//        return "SendMessageDto(roomId=$roomId, sender=$senderName, contents='$contents', sendTime=$sendTime)"
//    }
}