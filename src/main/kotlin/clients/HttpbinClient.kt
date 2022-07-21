package clients

import clients.models.HttpbinResponse
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class HttpbinClient @Inject constructor(private val client: Client) {
    suspend fun getSimpleResponse(): HttpbinResponse? = coroutineScope {
        val webTarget = client.target("https://httpbin.org/get")
        val invocation = webTarget.request(MediaType.APPLICATION_JSON)
        invocation.get(HttpbinResponse::class.java)
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
