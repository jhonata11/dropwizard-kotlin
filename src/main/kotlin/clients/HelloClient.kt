package clients

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

data class HttpBinHeaders(
    @JsonProperty("Host") val host: String
)

data class HttpBinResponse(
    @JsonProperty("url") val url: String,
    @JsonProperty("headers") val headers: HttpBinHeaders
)

class HelloClient @Inject constructor(private val client: Client) {
    suspend fun getSimpleResponse(): HttpBinResponse? = coroutineScope {
        val webTarget = client.target("https://httpbin.org/get")
        val invocation = webTarget.request(MediaType.APPLICATION_JSON)
        invocation.get(HttpBinResponse::class.java)
    }

    suspend fun getStatusResponse(status: Int) = coroutineScope {
        val webTarget = client.target("https://httpbin.org/status/$status")
        val invocation = webTarget.request()
        val result = invocation.get()
        when (result.status) {
            200 -> println()
            500 -> throw RuntimeException("Server error")
        }
    }

    suspend fun getDelayResponse(duration: Int) = coroutineScope {
        val webTarget = client.target("https://httpbin.org/delay/$duration")
        val invocation = webTarget.request()
        val result = invocation.get()
        when (result.status) {
            200 -> println()
            500 -> throw RuntimeException("Server error")
        }
    }
}
