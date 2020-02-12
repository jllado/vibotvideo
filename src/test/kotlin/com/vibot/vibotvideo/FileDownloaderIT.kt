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
    private val downloadTarget = "image.jpg"

    private lateinit var downloadedFile: File

    @After
    fun tearDown() {
        deleteAllFiles()
    }

    @Test
    fun `should download file`() {
        val fileUrl = "http://newnation.sg/wp-content/uploads/random-pic-internet-22.jpg"

        downloadedFile = fileDownloader.download(fileUrl, downloadDirectory, downloadTarget)

        assertThat(downloadedFile.exists(), `is`(true))
    }

    @Test
    fun `should download image with insecure https image`() {
        val fileUrl = "https://www.rumbonuevo.com.mx/wp-content/uploads/2019/05/LOZOYA-1.jpg"

        downloadedFile = fileDownloader.download(fileUrl, downloadDirectory, downloadTarget)

        assertThat(downloadedFile.exists(), `is`(true))
    }

    private fun deleteAllFiles() {
        FileSystemUtils.deleteRecursively(Paths.get("$downloadRootDirectory"))
    }
}