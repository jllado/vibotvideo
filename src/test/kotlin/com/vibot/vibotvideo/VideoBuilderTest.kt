package com.vibot.vibotvideo

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.lang.IllegalArgumentException

@RunWith(MockitoJUnitRunner::class)
class VideoBuilderTest {

    @Mock
    private lateinit var commandRunner: CommandRunner
    @Mock
    private lateinit var fileManager: FileManager

    @InjectMocks
    private lateinit var videoBuilder: VideoBuilder

    @Test(expected = IllegalArgumentException::class)
    fun `given ffmpeg command failure should throw exception`() {
        val images = 3
        val audioDuration = 100F
        val videoId = "any_id"
        val directory = File("directory")
        doReturn(directory).`when`(fileManager).mdkir("videos/$videoId")
        doReturn("100").`when`(commandRunner).run(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1",  AUDIO_FILE)
        doReturn("Conversion failed!").`when`(commandRunner).run(directory, "ffmpeg", "-y", "-framerate", "$images/$audioDuration", "-start_number", "1", "-i", "%d-image.jpg", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2","-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", "-shortest", "out.mp4")

        videoBuilder.build(videoId, images)
    }
}