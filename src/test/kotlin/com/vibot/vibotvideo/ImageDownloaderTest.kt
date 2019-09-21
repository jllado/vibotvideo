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
        val image1 = "https://images.pexels.com/photos/60597/dahlia-red-blossom-bloom-60597.jpeg"
        val image2 = "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"

        imageDownloader.download(listOf(image1, image2), directory)

        verify(fileDownloader).download(image1, directory, "1-image.jpg")
        verify(fileDownloader).download(image2, directory, "2-image.jpg")
    }

    @Test
    fun `given wrong image should skip it`() {
        val directory = "any_directory"
        val image1 = "https://images.pexels.com/photos/60597/dahlia-red-blossom-bloom-60597.jpeg"
        val image2 = "https://wtf"
        val image3 = "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"
        doThrow(IllegalStateException("wtf")).`when`(fileDownloader).download(image1, directory, "1-image.jpg")

        imageDownloader.download(listOf(image1, image2, image3), directory)

        verify(fileDownloader).download(image1, directory, "1-image.jpg")
        verify(fileDownloader).download(image3, directory, "2-image.jpg")
    }
}