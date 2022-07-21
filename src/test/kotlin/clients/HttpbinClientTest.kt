package clients

import AppConfiguration
import assertk.assertThat
import assertk.assertions.isEqualTo
import clients.models.HttpbinHeaders
import clients.models.HttpbinResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import javax.ws.rs.client.Client
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType

class HttpbinClientTest {
    private val httpClient: Client = mockk()
    private val webTarget: WebTarget = mockk()
    private val invocation: Invocation.Builder = mockk()

    private val config = AppConfiguration(httpbinBaseUrl = "base-url")
    private val sut = HttpbinClient(config, httpClient)

    @Test
    fun `it should load simple response successfully`(): Unit = runBlocking {
        every { httpClient.target("${config.httpbinBaseUrl}/get") } returns webTarget
        every { webTarget.request(MediaType.APPLICATION_JSON) } returns invocation
        every { invocation.get(HttpbinResponse::class.java) } returns HttpbinResponse("url", HttpbinHeaders("headers"))

        val response = sut.getSimpleResponse()
        assertThat(response.url).isEqualTo("url")
    }
}
