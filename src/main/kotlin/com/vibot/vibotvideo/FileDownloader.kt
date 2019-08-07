package com.vibot.vibotvideo

import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import java.io.File
import java.net.URL

@Service
class FileDownloader {

    fun download(url: String, directory: String, file: String): File {
        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        File(directory).mkdir()
        val newFile = File("$directory/$file")
        FileUtils.copyURLToFile(URL(url), newFile)
        return newFile
    }
}
