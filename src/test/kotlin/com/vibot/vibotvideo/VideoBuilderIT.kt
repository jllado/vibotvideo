package com.vibot.vibotvideo

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Paths

private const val VIDEO_ID = "temporal_id"

class VideoBuilderIT {

    private val builder = VideoBuilder(CommandRunner(), FileManager())

    private val video = File("videos/$VIDEO_ID/out.mp4")

    @Before
    fun setUp() {
        createMediaFiles()
    }

    @After
    fun tearDown() {
        deleteAllFiles()
    }

    @Test
    fun `should build video`() {
        val images = 3

        builder.build(VIDEO_ID, images)

        assertThat(video.exists(), `is`(true))
        assertThat(video.length(), `is`(7698532L))
    }

    private fun createMediaFiles() {
        val source = Paths.get("src/test/resources/video_media")
        val target = Paths.get("videos/$VIDEO_ID")
        FileSystemUtils.copyRecursively(source, target)
    }

    private fun deleteAllFiles() {
        FileSystemUtils.deleteRecursively(Paths.get("videos"))
    }
}