package com.vibot.vibotvideo.api

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VideoAPITest {

    @Mock
    private lateinit var service: VideoService

    @InjectMocks
    private lateinit var api: VideoAPI

    @Test
    fun `should return video url`() {
        val request = VideoRequest("audio_url", listOf("image_url"))
        val urlResponse = UrlResponse("video_url")
        doReturn(urlResponse).`when`(service).buildVideo(request)

        assertThat(api.buildVideo(request), `is`(urlResponse))
    }
}