package com.suco.sucotalk.room.domain

data class RoomInfo(val id: Long?, val name: String){
    constructor(room:Room):this(room.id, room.name)
}