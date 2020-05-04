package com.vibot.vibotvideo

import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.threads.ThreadPoolExecutor
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextClosedEvent
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

@SpringBootApplication
class VibotvideoApplication  {

    @Bean
    fun gracefulShutdown(): GracefulShutdown {
        return GracefulShutdown()
    }

    @Bean
    fun webServerFactory(gracefulShutdown: GracefulShutdown?): ConfigurableServletWebServerFactory {
        val factory = TomcatServletWebServerFactory()
        factory.addConnectorCustomizers(gracefulShutdown)
        return factory
    }
}

private const val TIMEOUT = 240L

class GracefulShutdown : TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent?> {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(GracefulShutdown::class.java)
    }

    @Volatile
    private lateinit var connector: Connector

    override fun customize(connector: Connector) {
        this.connector = connector
    }

    override fun onApplicationEvent(event: ContextClosedEvent) {
        LOGGER.info("Shutdown requested")
        shutdownRequestThreads()
        LOGGER.info("Shutdown")
    }

    private fun shutdownRequestThreads() {
        connector.pause()
        val threadPoolExecutor: Executor = connector.protocolHandler.executor
        if (threadPoolExecutor is ThreadPoolExecutor) {
            try {
                threadPoolExecutor.shutdown()
                if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    LOGGER.warn("Tomcat thread pool did not shut down gracefully within $TIMEOUT seconds. Proceeding with forceful shutdown")
                }
                threadPoolExecutor.shutdownNow();
                if (!threadPoolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    LOGGER.error("Tomcat thread pool did not terminate");
                }
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<VibotvideoApplication>(*args)
}
