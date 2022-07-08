package services

import clients.HelloClient
import clients.HttpBinResponse
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelloService @Inject constructor(private val client: HelloClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    suspend fun getSimpleResponse(): HttpBinResponse {
        return client.getSimpleResponse() ?: throw RuntimeException("User not found")
    }

    suspend fun getStatusResponse(status: Int) {
        client.getStatusResponse(status)
    }
}

