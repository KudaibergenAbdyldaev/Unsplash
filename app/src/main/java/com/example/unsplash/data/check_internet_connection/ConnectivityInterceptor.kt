package com.example.unsplash.data.check_internet_connection

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class ConnectivityInterceptor @Inject constructor(private val networkUtil: NetworkUtil) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkUtil.isInternetAvailable()) {
            throw NoConnectivityException()
        }

        val newRequest: Request

        val request = chain.request()
        newRequest = request.newBuilder()
            .build()
        return chain.proceed(newRequest)
    }
}