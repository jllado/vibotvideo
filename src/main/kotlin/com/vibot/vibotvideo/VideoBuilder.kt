package com.vibot.vibotvideo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.ceil
import kotlin.math.roundToInt

const val AUDIO_FILE = "audio.wav"
const val MUSIC_FILE = "music.mp3"
const val MUSIC_LOOP_FILE = "loop.mp3"
const val VIDEO_TMP = "tmp.mp4"
const val VIDEO_FILE = "out.mp4"

private  val ffmpegErrors = listOf("conversion failed", "invalid argument", "no such file", "Stream map '0:v' matches no streams")

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
        val duration = getAudioDuration(directory, AUDIO_FILE)
        val frameRate = "$images/$duration"
        LOGGER.info("Building video. Framerate: {}, Audio duration: {} secs, Images: {}", frameRate, duration, images)
        buildVideo(directory, frameRate)
        addMusic(directory, duration)
    }

    private fun addMusic(directory: File, videoDuration: Float) {
        val music = File("./music/news.mp3")
        fileManager.copy(music, File("${directory.path}/$MUSIC_FILE"))
        val musicDuration = getAudioDuration(directory, MUSIC_FILE)
        val loopTimes = musicLoopTimes(videoDuration, musicDuration)
        buildMusicLoop(directory, loopTimes, videoDuration)
        val musicLoopDuration = getAudioDuration(directory, MUSIC_LOOP_FILE)
        mergeMusicWithVideo(directory, videoDuration, musicLoopDuration)
    }

    //ffmpeg -y -i tmp.mp4 -i loop.mp3 -filter_complex "[0:a][1:a]amerge=inputs=2[a]" -map 0:v -map "[a]" -c:v copy -c:a libmp3lame -ac 2 -strict -1 out.mp4
    private fun mergeMusicWithVideo(directory: File, videoDuration: Float, musicLoopDuration: Float) {
        try {
            LOGGER.info("Merging video {} sesc and music {} secs", videoDuration, musicLoopDuration)
            val result = commandRunner.run(directory, "ffmpeg", "-y", "-i", VIDEO_TMP, "-i", MUSIC_LOOP_FILE, "-filter_complex", "[0:a][1:a]amerge=inputs=2[a]", "-map", "0:v", "-map", "[a]", "-c:v", "copy", "-c:a", "libmp3lame", "-ac", "2", "-strict", "-1", VIDEO_FILE)
            checkErros(result)
            LOGGER.info("Video and music merged")
        } catch (e: Exception) {
            LOGGER.error("Video and music merge failed", e)
            commandRunner.run(directory, "cp", VIDEO_TMP, VIDEO_FILE)
            LOGGER.error("Video builded without music", e)
        }
    }

    //sox -v 0.3 Arpy.mp3 new_audio.mp3 repeat 10
    private fun buildMusicLoop(directory: File, times: Int, duration: Float) {
        LOGGER.info("Building music loop")
        val durationWithOverhead = duration.roundToInt() + 3 //1 sec longer than video
        commandRunner.run(directory, "sox", "-v", " 0.05", "music.mp3", MUSIC_LOOP_FILE, "repeat", (times - 1).toString(), "trim", "0", "$durationWithOverhead")
        LOGGER.info("Music loop builded")
    }

    //ffmpeg -y -thread_queue_size 128 -framerate 3/314 -start_number 1 -i %d-image.png -i audio.wav -c:v libx264 -r 1 -pix_fmt yuv420p -c:a aac -strict experimental -shortest out.mp4
    private fun buildVideo(directory: File, frameRate: String) {
        LOGGER.info("Building video")
        val result = commandRunner.run(directory, "ffmpeg", "-y", "-framerate", frameRate, "-start_number", "1", "-i", "%d-image.png", "-i", AUDIO_FILE, "-c:v", "libx264", "-r", "1", "-vf", "scale=trunc(iw/2)*2:trunc(ih/2)*2", "-pix_fmt", "yuv420p", "-c:a", "aac", "-strict", "experimental", VIDEO_TMP)
        checkErros(result)
        LOGGER.info("Video builded")
    }

    private fun checkErros(result: String) {
        if (ffmpegErrors.any { result.contains(it, true) }) {
            throw IllegalArgumentException("Build video failure")
        }
    }

    //ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 audio.wav
    private fun getAudioDuration(directory: File, audio: String) = commandRunner.run(directory, "ffprobe",  "-v",  "error",  "-show_entries", "format=duration", "-of" , "default=noprint_wrappers=1:nokey=1", audio).toFloat()
}

fun musicLoopTimes(videoDuration: Float, musicDuration: Float) = ceil(videoDuration / musicDuration).toInt()
