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
        val audioUrl = "https://rhythm-lab.com/sstorage/53/2019/12/4th%20Coming%20-%20You%20Don't%20Stand%20a%20Chance,%20Pt.%202.wav"
        val image1 = "https://pluspng.com/img-png/search-hd-png-seo-png-hd-png-image-1270.png"
        val image2 = "https://moodle.inasp.info/pluginfile.php/200987/mod_folder/content/0/3.18%20Advanced%20search.png"
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