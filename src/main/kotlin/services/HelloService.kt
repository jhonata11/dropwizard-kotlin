package services

import clients.HelloClient
import clients.HttpBinResponse
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

enum class RequestType {
    DELAY, STATUS, SIMPLE
}

@Singleton
class HelloService @Inject constructor(private val client: HelloClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    suspend fun getSimpleResponse(type: RequestType?, payload: Int?): HttpBinResponse? {
        return when (type) {
            RequestType.SIMPLE, null -> client.getSimpleResponse()
            RequestType.DELAY -> {
                client.getDelayResponse(payload ?: 0)
                null
            }
            RequestType.STATUS -> {
                client.getStatusResponse(payload ?: 0)
                null
            }
        }
    }
}
