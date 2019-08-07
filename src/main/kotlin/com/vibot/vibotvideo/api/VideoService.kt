package com.vibot.vibotvideo.api

import com.vibot.vibotvideo.AUDIO_FILE
import com.vibot.vibotvideo.FileDownloader
import com.vibot.vibotvideo.ImageDownloader
import com.vibot.vibotvideo.VideoBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VideoService @Autowired constructor(
        private val idBuilder: VideoIdBuilder,
        private val fileDownloader: FileDownloader,
        private val imageDownloader: ImageDownloader,
        private val videoBuilder: VideoBuilder
){

    fun buildVideo(request: VideoRequest): UrlResponse {
        val videoId = idBuilder.build()
        val directory = "videos/$videoId"
        downloadAudio(request.audio, directory)
        downloadImages(request, directory)
        videoBuilder.build(videoId, request.images.size)
        return UrlResponse("/video/$videoId")
    }

    private fun downloadImages(request: VideoRequest, directory: String) {
        imageDownloader.download(request.images, directory)
    }

    private fun downloadAudio(url: String, directory: String) {
        fileDownloader.download(url, directory, AUDIO_FILE)
    }
}
