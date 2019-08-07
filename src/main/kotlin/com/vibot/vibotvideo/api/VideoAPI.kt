package com.vibot.vibotvideo.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileInputStream

@RestController
class VideoAPI @Autowired constructor(
        private val service: VideoService
){

    @PostMapping("/buildVideo")
    fun buildVideo(@RequestBody request: VideoRequest): UrlResponse {
        return service.buildVideo(request)
    }

    @GetMapping(value = ["/video/{id}"], produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @ResponseBody
    fun getVideo(@PathVariable("id") id: String): ResponseEntity<Resource> {
        val file = File("videos/$id/out.mp4")
        val resource = InputStreamResource(FileInputStream(file))
        return ResponseEntity.ok().body(resource)
    }
}