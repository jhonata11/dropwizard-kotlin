package clients

import AppConfiguration
import clients.models.HttpbinResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriBuilder

class HttpbinClient @Inject constructor(
    private val config: AppConfiguration,
    private val client: Client
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun getResponse(name: String, delay: Int): HttpbinResponse {
        val path = "${config.httpbinBaseUrl}/delay/$delay"
        val uri = UriBuilder.fromUri(path).queryParam("name", name).build()
        val webTarget = client.target(uri)
        val response = webTarget.request().apply {
            acceptEncoding(MediaType.APPLICATION_JSON)
        }.get(HttpbinResponse::class.java)
        logger.info("$name - OK")
        return response
    }
}
