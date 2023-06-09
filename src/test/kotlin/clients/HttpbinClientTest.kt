package clients

import AppConfiguration
import assertk.assertThat
import assertk.assertions.isEqualTo
import clients.models.HttpBinArgs
import clients.models.HttpbinResponse
import io.mockk.every
import io.mockk.mockk
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.Invocation
import jakarta.ws.rs.client.WebTarget
import jakarta.ws.rs.core.MediaType
import org.junit.jupiter.api.Test
import java.net.URI

class HttpbinClientTest {
    private val config = AppConfiguration(httpbinBaseUrl = "http://testurl.com")
    private val client = mockk<Client>()
    private val target = mockk<WebTarget>()
    private val invocationBuilder = mockk<Invocation.Builder>()

    @Test
    fun `it should load delayed response successfully`() {
        every { client.target(any<URI>()) } returns target
        every { target.request() } returns invocationBuilder
        every { invocationBuilder.acceptEncoding(MediaType.APPLICATION_JSON) } returns invocationBuilder
        every { invocationBuilder.get(HttpbinResponse::class.java) } returns HttpbinResponse("http://url", HttpBinArgs("request-name"))

        val sut = HttpbinClient(config, client)

        val response = sut.getResponse("request-name", 10)
        assertThat(response.url).isEqualTo("http://url")
        assertThat(response.args.name).isEqualTo("request-name")
    }
}
