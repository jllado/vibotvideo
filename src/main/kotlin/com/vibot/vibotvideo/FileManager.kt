package com.vibot.vibotvideo

import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.File

@Service
class FileManager {

    fun mdkir(path: String): File {
        val directory = File(path)
        directory.mkdirs()
        return directory
    }

    fun copy(file: File, directory: File) {
        FileCopyUtils.copy(file, directory)
    }
}
