package com.vibot.vibotvideo.api

import com.vibot.vibotvideo.FileDownloader
import com.vibot.vibotvideo.ImageDownloader
import com.vibot.vibotvideo.VideoBuilder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VideoServiceTest {

    @Mock
    private lateinit var downloader: FileDownloader
    @Mock
    private lateinit var imageDownloader: ImageDownloader
    @Mock
    private lateinit var idBuilder: VideoIdBuilder
    @Mock
    private lateinit var videoBuilder: VideoBuilder

    @InjectMocks
    private lateinit var service: VideoService

    @Test
    fun `given video request should download audio`() {
        val audioUrl = "audio_url"
        val videoId = "video_id"
        doReturn(videoId).`when`(idBuilder).build()

        service.buildVideo(VideoRequest(audioUrl, listOf("any_image")))

        verify(downloader).download(audioUrl, "videos/$videoId", "audio.wav")
    }

    @Test
    fun `given video request should download images`() {
        val videoId = "video_id"
        val directory = "videos/$videoId"
        val image1 = "image1"
        val image2 = "image2"
        doReturn(videoId).`when`(idBuilder).build()

        service.buildVideo(VideoRequest("audio_url", listOf(image1, image2)))

        verify(imageDownloader).download(listOf(image1, image2), directory)
    }

    @Test
    fun `given video request should build video`() {
        val videoId = "video_id"
        doReturn(videoId).`when`(idBuilder).build()
        val request = VideoRequest("audio_url", listOf("image1", "image2"))

        service.buildVideo(request)

        verify(videoBuilder).build(videoId, 2)
    }

    @Test
    fun `given video request should return video url`() {
        val videoId = "video_id"
        doReturn(videoId).`when`(idBuilder).build()
        val request = VideoRequest("audio_url", listOf("image1", "image2"))

        val response = service.buildVideo(request)

        assertThat(response.url, `is`("/video/$videoId"))
    }
}