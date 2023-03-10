package clients

import App
import AppConfiguration
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import ru.vyarus.dropwizard.guice.test.jupiter.TestGuiceyApp
import javax.inject.Inject
import javax.ws.rs.ProcessingException
import javax.ws.rs.client.Client

@TestGuiceyApp(App::class)
class HttpbinClientTest {

    @RegisterExtension
    private val wiremock = WireMockExtension.newInstance().apply {
        options(wireMockConfig().dynamicPort())
    }.build()

    @Inject
    private lateinit var client: Client

    private val sut: HttpbinClient by lazy {
        val config = AppConfiguration(httpbinBaseUrl = wiremock.baseUrl())
        return@lazy HttpbinClient(config, client)
    }

    @Test
    fun `it should load simple response successfully`() {
        wiremock.stubFor(
            get("/get").willReturn(
                okJson(
                    """{
                          "args": {}, 
                          "headers": {
                            "Host": "httpbin.org"
                          }, 
                          "origin": "82.129.115.49", 
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                )
            )
        )

        val response = runBlocking { sut.getSimpleResponse() }
        assertThat(response.url).isEqualTo("https://httpbin.org/get")
        assertThat(response.headers.host).isEqualTo("httpbin.org")
    }

    @Test
    fun `it times out after`() {
        wiremock.stubFor(
            get("/get").willReturn(
                okJson(
                    """{
                          "headers": {
                            "Host": "httpbin.org"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                ).withFixedDelay(501)
            )
        )
        assertThat { runBlocking { sut.getSimpleResponse() } }
            .isFailure()
            .isInstanceOf(ProcessingException::class.java)
            .hasMessage("java.net.SocketTimeoutException: Read timed out")
    }

    @Test
    fun `it doesn't timeout`() {
        wiremock.stubFor(
            get("/get").willReturn(
                okJson(
                    """{
                          "headers": {
                            "Host": "httpbin.org"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                ).withFixedDelay(100)
            )
        )
        val response = runBlocking { sut.getSimpleResponse() }
        assertThat(response.url).isEqualTo("https://httpbin.org/get")
        assertThat(response.headers.host).isEqualTo("httpbin.org")
    }
}
