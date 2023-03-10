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

    private val config = AppConfiguration(httpbinBaseUrl = "http://testurl.com")
    private val client = mockk<Client>()
    private val target = mockk<WebTarget>()
    private val invocationBuilder = mockk<Invocation.Builder>()

    @Test
    fun `it should load simple response successfully`() {
        every { client.target("http://testurl.com/get") } returns target
        every { target.request() } returns invocationBuilder
        every { invocationBuilder.acceptEncoding(MediaType.APPLICATION_JSON) } returns invocationBuilder
        every { invocationBuilder.get(HttpbinResponse::class.java) } returns HttpbinResponse("test", HttpbinHeaders("host"))

        val sut = HttpbinClient(config, client)

        val response = runBlocking { sut.getSimpleResponse() }
        assertThat(response.url).isEqualTo("test")
        assertThat(response.headers.host).isEqualTo("host")
    }

    @Test
    fun `it should load delayed response successfully`() {
        every { client.target("http://testurl.com/delay/10") } returns target
        every { target.request() } returns invocationBuilder
        every { invocationBuilder.acceptEncoding(MediaType.APPLICATION_JSON) } returns invocationBuilder
        every { invocationBuilder.get(HttpbinResponse::class.java) } returns HttpbinResponse("test", HttpbinHeaders("host"))

        val sut = HttpbinClient(config, client)

        val response = runBlocking { sut.getDelayedResponse() }
        assertThat(response.url).isEqualTo("test")
        assertThat(response.headers.host).isEqualTo("host")
    }
}
