package services

import api.req.RequestType
import api.res.HelloResponse
import clients.HttpbinClient
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelloService @Inject constructor(private val client: HttpbinClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    suspend fun getSimpleResponse(type: RequestType?, payload: Int?): HelloResponse? {
        return when (type) {
            RequestType.SIMPLE, null -> {
                val response = client.getSimpleResponse() ?: throw RuntimeException("Wrong response type")
                HelloResponse(example = response.url)
            }
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
