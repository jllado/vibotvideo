package com.vibot.vibotvideo

import org.springframework.stereotype.Service
import java.io.File

@Service
class FileManager {

    fun mdkir(path: String): File {
        val directory = File(path)
        directory.mkdirs()
        return directory
    }
}
