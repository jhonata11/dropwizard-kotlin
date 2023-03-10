package clients

import AppConfiguration
import clients.models.HttpbinResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.client.Client
import javax.ws.rs.core.MediaType

class HttpbinClient @Inject constructor(
    private val config: AppConfiguration,
    private val client: Client
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    suspend fun getSimpleResponse(): HttpbinResponse = withContext(Dispatchers.IO) {
        val webTarget = client.target("${config.httpbinBaseUrl}/get")
        val invocation = webTarget.request(MediaType.APPLICATION_JSON)
        val res = invocation.get(HttpbinResponse::class.java)
        logger.info("getSimpleResponse - OK")
        return@withContext res
    }

    suspend fun getDelayedResponse(): HttpbinResponse = withContext(Dispatchers.IO) {
        val webTarget = client.target("${config.httpbinBaseUrl}/delay/10")
        val invocation = webTarget.request(MediaType.APPLICATION_JSON)
        val res = invocation.get(HttpbinResponse::class.java)
        logger.info("getDelayedResponse - OK")
        return@withContext res
    }
}
