package services

import api.res.HelloResponse
import clients.HttpbinClient
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelloService @Inject constructor(private val client: HttpbinClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    suspend fun getSimpleResponse(): HelloResponse {
        val response = client.getSimpleResponse()
        return HelloResponse(example = response.url)
    }
}
