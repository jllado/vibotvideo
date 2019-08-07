package com.vibot.vibotvideo

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Paths

class ImageDownloaderTest {

    private val imageDownloader = ImageDownloader()

    private val rootDirectory = "temp_images"
    private val directory = "$rootDirectory/images"

    @After
    fun tearDown() {
        deleteAllFiles()
    }

    @Test
    fun `should download all images`() {
        val image1 = "https://images.pexels.com/photos/60597/dahlia-red-blossom-bloom-60597.jpeg"
        val image2 = "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"

        imageDownloader.download(listOf(image1, image2), directory)

        assertThat(File("$directory/1-image.jpg").exists(), `is`(true))
        assertThat(File("$directory/2-image.jpg").exists(), `is`(true))
    }

    private fun deleteAllFiles() {
        FileSystemUtils.deleteRecursively(Paths.get("$rootDirectory"))
    }
}