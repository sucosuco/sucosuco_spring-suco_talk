package com.suco.sucotalk

import com.suco.sucotalk.member.exception.MemberException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(Exception::class)
    fun handlerUnhandledException(): ResponseEntity<ExceptionResponseDto> {
        val message = "Oop.. There's unhandled exception"
        val exceptionResponse = ExceptionResponseDto(message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse)
    }

    @ExceptionHandler(MemberException::class)
    fun handleMemberException(e: RuntimeException): ResponseEntity<ExceptionResponseDto> {
        val exceptionResponse = ExceptionResponseDto(e.message)
        return ResponseEntity.badRequest().body(exceptionResponse)
    }
}