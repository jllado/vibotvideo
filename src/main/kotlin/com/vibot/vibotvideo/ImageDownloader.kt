package com.vibot.vibotvideo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageDownloader @Autowired constructor(
    private val fileDownloader: FileDownloader
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ImageDownloader::class.java)
    }

    fun download(images: List<String>, directory: String): Int {
        var downloaded = 0
        for (image in images) {
            try {
                fileDownloader.download(image, directory, "${downloaded + 1}-image.jpg")
                downloaded++
            } catch (e: Exception) {
                LOGGER.error("Image download failed {} with error: {}", image, e)
            }
        }
        return downloaded
    }
}
