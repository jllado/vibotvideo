package com.vibot.vibotvideo

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

const val AUDIO_FILE = "audio.wav"

@Service
class VideoBuilder {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(VideoBuilder::class.java)
    }

    fun build(videoId: String, images: Int) {
        val directory = createDirectory(videoId)
        val audioDuration = getAudioDuration(directory)
        val frameRate = "$images/$audioDuration"
        LOGGER.info("framerate: {}", frameRate)
        buildVideo(directory, frameRate)
    }

    //ffmpeg -y -thread_queue_size 128 -framerate 3/314 -start_number 1 -i %d-image.jpg -i audio.wav -c:v libx264 -r 1 -pix_fmt yuv420p -c:a aac -strict experimental -shortest out.mp4
    private fun buildVideo(directory: File, frameRate: String) {
        command(directory, "ffmpeg", "-y", "-framerate", frameRate, "-start_number", "1", "-i", "%d-image.jpg", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", "-shortest", "out.mp4")
    }

    private fun command(directory: File, vararg command: String): String {
        val processBuilder = ProcessBuilder()
        processBuilder.redirectErrorStream(true)
        processBuilder.command(*command)
        processBuilder.directory(directory)
        val process = processBuilder.start()
        val output = process.printLog()
        process.waitFor()
        return output
    }

    private fun createDirectory(videoId: String): File {
        val directory = File("videos/$videoId")
        directory.mkdirs()
        return directory
    }

    //ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 audio.wav
    private fun getAudioDuration(directory: File) = command(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1",  AUDIO_FILE).toFloat()

    private fun Process.printLog() = BufferedReader(InputStreamReader(inputStream)).readLines().map { LOGGER.info(it); it }.joinToString("")
}
