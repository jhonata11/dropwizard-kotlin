package clients

import AppConfiguration
import clients.models.HttpbinResponse
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class HttpbinClient @Inject constructor(private val config: AppConfiguration, private val client: Client) {
    suspend fun getSimpleResponse(): HttpbinResponse = coroutineScope {
        val webTarget = client.target("${config.httpbinBaseUrl}/get")
        val invocation = webTarget.request(MediaType.APPLICATION_JSON)
        invocation.get(HttpbinResponse::class.java)
    }
}
