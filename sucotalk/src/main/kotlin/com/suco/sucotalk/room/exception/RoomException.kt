package com.suco.sucotalk.room.exception

import java.lang.RuntimeException

class RoomException(override val message: String?) : RuntimeException(message)