package com.vibot.vibotvideo

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Service
class CommandRunner {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CommandRunner::class.java)
    }

    fun run(directory: File, vararg command: String): String {
        val processBuilder = ProcessBuilder()
        processBuilder.redirectErrorStream(true)
        processBuilder.command(*command)
        processBuilder.directory(directory)
        val process = processBuilder.start()
        val output = process.printLog()
        process.waitFor()
        LOGGER.info(output)
        return output
    }

    private fun Process.printLog() = BufferedReader(InputStreamReader(inputStream)).readLines().joinToString("\n")
}
