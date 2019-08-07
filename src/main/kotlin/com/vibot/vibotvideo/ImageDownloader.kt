package com.vibot.vibotvideo

import org.springframework.stereotype.Service

@Service
class ImageDownloader {

    private val fileDownloader = FileDownloader()

    fun download(urls: List<String>, directory: String) {
        urls.forEachIndexed { index, image ->
            fileDownloader.download(image, directory, "${index+1}-image.jpg")
        }
    }
}
