package com.vibot.vibotvideo.api

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service

@Service
class VideoIdBuilder {

    fun build(): String = RandomStringUtils.randomAlphabetic(8)
}
