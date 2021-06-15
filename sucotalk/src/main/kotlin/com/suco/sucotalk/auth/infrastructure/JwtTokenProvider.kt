package com.suco.sucotalk.auth.infrastructure

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {
    @Value("\${security.jwt.token.secret-key}")
    private val secretKey: String? = null

    @Value("\${security.jwt.token.expire-length}")
    private val validityInMilliseconds: Long = 0

    fun createToken(payload: String?): String? {
        val claims = Jwts.claims().setSubject(payload)
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getPayload(token: String?): String {
        validateToken(token)
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(token: String?) {
        try {
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            validateExpiration(claims)
        } catch (e: JwtException) {
        } catch (e: IllegalArgumentException) {
        }
    }

    private fun validateExpiration(claims: Jws<Claims>) {
        val expiration = claims.body.expiration
        if (expiration.before(Date())) {
        }
    }
}