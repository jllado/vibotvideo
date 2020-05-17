package com.vibot.vibotvideo

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalStateException

@RunWith(MockitoJUnitRunner::class)
class ImageDownloaderTest {

    @Mock
    private lateinit var fileDownloader: FileDownloader

    @InjectMocks
    private lateinit var imageDownloader: ImageDownloader

    @Test
    fun `should download all images`() {
        val directory = "any_directory"
        val image1 = "https://pluspng.com/img-png/search-hd-png-seo-png-hd-png-image-1270.png"
        val image2 = "https://moodle.inasp.info/pluginfile.php/200987/mod_folder/content/0/3.18%20Advanced%20search.png"

        imageDownloader.download(listOf(image1, image2), directory)

        verify(fileDownloader).download(image1, directory, "1-image.png")
        verify(fileDownloader).download(image2, directory, "2-image.png")
    }

    @Test
    fun `given wrong image should skip it`() {
        val directory = "any_directory"
        val image1 = "https://moodle.inasp.info/pluginfile.php/200987/mod_folder/content/0/3.18%20Advanced%20search.png"
        val image2 = "https://wtf.url"
        val image3 = "https://pluspng.com/img-png/search-hd-png-seo-png-hd-png-image-1270.png"
        doThrow(IllegalStateException("wtf")).`when`(fileDownloader).download(image1, directory, "1-image.png")

        imageDownloader.download(listOf(image1, image2, image3), directory)

        verify(fileDownloader).download(image1, directory, "1-image.png")
        verify(fileDownloader).download(image3, directory, "2-image.png")
    }
}