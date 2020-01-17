package co.artechinfo.shekhar.networking

import java.io.IOException

/*
* NoConnectivityException class
* exception class for connectivity
* */
class NoConnectivityException : IOException() {

    override val message: String
        get() = "No network available, please check your WiFi or Data connection"
}