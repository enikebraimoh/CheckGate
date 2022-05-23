package com.escrowafrica.checkgate.ui.network

import com.escrowafrica.checkgate.ui.util.App
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class TokenInterceptor
@Inject
    constructor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.header("No-Authentication") == null) {
            if (!App.token.isNullOrEmpty()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer ${App.token}")
                    .build()
            }
        }
        return chain.proceed(request)
    }

}