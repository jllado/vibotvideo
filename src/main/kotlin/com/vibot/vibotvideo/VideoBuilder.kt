package com.vibot.vibotvideo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

const val AUDIO_FILE = "audio.wav"

@Service
class VideoBuilder @Autowired constructor(
        private val commandRunner: CommandRunner,
        private val fileManager: FileManager
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(VideoBuilder::class.java)
    }

    fun build(videoId: String, images: Int) {
        val directory = fileManager.mdkir("videos/$videoId")
        val audioDuration = getAudioDuration(directory)
        val frameRate = "$images/$audioDuration"
        LOGGER.info("framerate: {}", frameRate)
        buildVideo(directory, frameRate)
    }

    //ffmpeg -y -thread_queue_size 128 -framerate 3/314 -start_number 1 -i %d-image.jpg -i audio.wav -c:v libx264 -r 1 -pix_fmt yuv420p -c:a aac -strict experimental -shortest out.mp4
    private fun buildVideo(directory: File, frameRate: String) {
        val result = commandRunner.run(directory, "ffmpeg", "-y", "-framerate", frameRate, "-start_number", "1", "-i", "%d-image.jpg", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2", "-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", "-shortest", "out.mp4")
        if (result.contains("conversion failed", ignoreCase = true)) {
            throw IllegalArgumentException("Build video failure")
        }
    }

    //ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 audio.wav
    private fun getAudioDuration(directory: File) = commandRunner.run(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1",  AUDIO_FILE).toFloat()

}
