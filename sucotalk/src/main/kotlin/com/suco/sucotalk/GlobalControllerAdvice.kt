package com.suco.sucotalk

import com.suco.sucotalk.member.exception.MemberException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(Exception::class)
    fun handlerUnhandledException(): ResponseEntity<String> {
        val message = "Oop.. There's unhandled exception"
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message)
    }

    @ExceptionHandler(MemberException::class)
    fun handleMemberException(e: RuntimeException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}