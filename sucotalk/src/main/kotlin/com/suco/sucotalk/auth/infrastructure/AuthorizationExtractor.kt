package com.suco.sucotalk.auth.infrastructure

import javax.servlet.http.HttpServletRequest

object AuthorizationExtractor {
    const val AUTHORIZATION = "Authorization"
    val ACCESS_TOKEN_TYPE = AuthorizationExtractor::class.java.simpleName + ".ACCESS_TOKEN_TYPE"
    var BEARER_TYPE = "Bearer"

    fun extract(request: HttpServletRequest): String? {
        val headers = request.getHeaders(AUTHORIZATION)
        while (headers.hasMoreElements()) {
            val value = headers.nextElement()
            if (value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
                var authHeaderValue = value.substring(BEARER_TYPE.length).trim { it <= ' ' }
                request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length).trim { it <= ' ' })
                val commaIndex = authHeaderValue.indexOf(',')
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex)
                }
                return authHeaderValue
            }
        }
        return null
    }
}
