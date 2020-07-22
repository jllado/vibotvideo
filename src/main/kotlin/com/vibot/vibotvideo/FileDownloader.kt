package com.vibot.vibotvideo

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.security.cert.X509Certificate
import javax.imageio.ImageIO
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Service
class FileDownloader {

    fun download(url: String, directory: String, file: String): File {
        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        useInsecureSSL()
        File(directory).mkdir()
        val newFile = File("$directory/$file")
        FileUtils.copyURLToFile(URL(url), newFile, 5000, 5000)
        return newFile
    }

    private fun useInsecureSSL() {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
        })

        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        // Create all-trusting host name verifier
        val allHostsValid = HostnameVerifier { _, _ -> true }

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }
}
