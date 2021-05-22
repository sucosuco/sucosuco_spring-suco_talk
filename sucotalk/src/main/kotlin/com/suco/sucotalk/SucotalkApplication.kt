package com.suco.sucotalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SucotalkApplication

fun main(args: Array<String>) {
	println(test)
	runApplication<SucotalkApplication>(*args)
}
