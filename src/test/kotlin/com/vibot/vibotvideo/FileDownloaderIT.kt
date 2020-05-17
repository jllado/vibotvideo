package com.vibot.vibotvideo

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Paths

class FileDownloaderIT {

    private val fileDownloader = FileDownloader()

    private val downloadRootDirectory = "temp_download"
    private val downloadDirectory = "$downloadRootDirectory/dfasdf"
    private val downloadTarget = "image.png"

    private lateinit var downloadedFile: File

    @After
    fun tearDown() {
        deleteAllFiles()
    }

    @Test
    fun `should download file`() {
        val fileUrl = "https://pluspng.com/img-png/search-hd-png-seo-png-hd-png-image-1270.png"

        downloadedFile = fileDownloader.download(fileUrl, downloadDirectory, downloadTarget)

        assertThat(downloadedFile.exists(), `is`(true))
    }

    @Test
    fun `should download image with insecure https image`() {
        val fileUrl = "https://pluspng.com/img-png/search-hd-png-seo-png-hd-png-image-1270.png"

        downloadedFile = fileDownloader.download(fileUrl, downloadDirectory, downloadTarget)

        assertThat(downloadedFile.exists(), `is`(true))
    }

    private fun deleteAllFiles() {
        FileSystemUtils.deleteRecursively(Paths.get("$downloadRootDirectory"))
    }
}