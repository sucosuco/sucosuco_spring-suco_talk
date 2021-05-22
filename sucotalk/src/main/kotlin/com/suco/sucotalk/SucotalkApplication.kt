package com.suco.sucotalk

import com.suco.sucotalk.member.domain.Member
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SucotalkApplication

fun main(args: Array<String>) {
	runApplication<SucotalkApplication>(*args)
}
