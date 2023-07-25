package com.example.unsplash.data.check_internet_connection

import java.io.IOException

class NoConnectivityException : IOException() {
    override fun getLocalizedMessage(): String? {
        return "No connectivity exception"
    }
}