package com.vibot.vibotvideo

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class VideoBuilderTest {

    @Mock
    private lateinit var commandRunner: CommandRunner
    @Mock
    private lateinit var fileManager: FileManager

    @InjectMocks
    private lateinit var videoBuilder: VideoBuilder

    private val images = 3
    private val audioDuration = 100F
    private val videoId = "any_id"
    private val directory = File("directory")

    @Test(expected = IllegalArgumentException::class)
    fun `given conversion ffmpeg failure should throw exception`() {
        doReturn(directory).`when`(fileManager).mdkir("videos/$videoId")
        doReturn("100").`when`(commandRunner).run(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1",  AUDIO_FILE)
        doReturn("adsf asdf as Conversion failed! adfasdf adfas").`when`(commandRunner).run(directory, "ffmpeg", "-y", "-framerate", "$images/$audioDuration", "-start_number", "1", "-i", "%d-image.jpg", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2","-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", "-shortest", VIDEO_TMP)

        videoBuilder.build(videoId, images)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `given invalid argument ffmpeg error should throw exception`() {
        doReturn(directory).`when`(fileManager).mdkir("videos/$videoId")
        doReturn("100").`when`(commandRunner).run(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1",  AUDIO_FILE)
        doReturn("adsf asdf as Invalid argument adfasdf adfas").`when`(commandRunner).run(directory, "ffmpeg", "-y", "-framerate", "$images/$audioDuration", "-start_number", "1", "-i", "%d-image.jpg", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2","-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", "-shortest", VIDEO_TMP)

        videoBuilder.build(videoId, images)
    }

    @Test
    fun `should calculate loop times`() {
        assertThat(musicLoopTimes(313.864000F, 247.405714F), `is`(2))
    }
}