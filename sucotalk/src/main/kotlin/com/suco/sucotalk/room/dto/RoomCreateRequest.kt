package com.suco.sucotalk.room.dto

data class RoomCreateRequest(val name: String, val members: List<Long>)