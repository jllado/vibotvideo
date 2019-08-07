package com.vibot.vibotvideo.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.FileSystemUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class VideoApiIT {

    @Autowired
    private lateinit var mvc: MockMvc

    @After
    fun tearDown() {
        deleteAllFiles()
    }

    @Test
    fun `send video audio and images urls should return video url`() {
        val audioUrl = "https://file-examples.com/wp-content/uploads/2017/11/file_example_WAV_1MG.wav"
        val image1 = "https://images.pexels.com/photos/60597/dahlia-red-blossom-bloom-60597.jpeg"
        val image2 = "https://images.pexels.com/photos/67636/rose-blue-flower-rose-blooms-67636.jpeg"
        val post = "{\"audio\": \"$audioUrl\",\"images\": [\"$image1\",\"$image2\"]}"
        val urlResponseResult = mvc.perform(post("/buildVideo").contentType(MediaType.APPLICATION_JSON_UTF8).content(post))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.url", `is`(notNullValue()))).andReturn()
        val urlResponse = ObjectMapper().registerModule(KotlinModule()).readValue(urlResponseResult.response.contentAsString, UrlResponse::class.java)
        mvc.perform(get(urlResponse.url).contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(status().isOk)
    }

    private fun deleteAllFiles() {
        FileSystemUtils.deleteRecursively(Paths.get("videos"))
    }
}