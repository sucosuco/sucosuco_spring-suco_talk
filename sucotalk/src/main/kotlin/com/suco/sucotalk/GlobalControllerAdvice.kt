package com.suco.sucotalk

import com.suco.sucotalk.auth.exception.AuthException
import com.suco.sucotalk.member.exception.MemberException
import com.suco.sucotalk.room.exception.RoomException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalControllerAdvice {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    @ExceptionHandler(MemberException::class, RoomException::class)
    fun handleDomainException(e: RuntimeException): ResponseEntity<ExceptionResponseDto> {
        val exceptionResponse = ExceptionResponseDto(e.message)
        return ResponseEntity.badRequest().body(exceptionResponse)
    }

    @ExceptionHandler(AuthException::class)
    fun handleAuthorizationException(e: RuntimeException): ResponseEntity<ExceptionResponseDto> {
        val exceptionResponse = ExceptionResponseDto(e.message)
        return ResponseEntity.badRequest().body(exceptionResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalidRequestArgument(bindingResult: BindingResult): ResponseEntity<ExceptionResponseDto> {
        val message = bindingResult.fieldError?.defaultMessage
        val exceptionResponse = ExceptionResponseDto(message)
        return ResponseEntity.badRequest().body(exceptionResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handlerUnhandledException(e: Exception): ResponseEntity<ExceptionResponseDto> {
        logger.error(e.message)

        val message = "Oops.. There's unhandled exception"
        val exceptionResponse = ExceptionResponseDto(message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse)
    }
}