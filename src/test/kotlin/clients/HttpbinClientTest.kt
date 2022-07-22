package clients

import AppConfiguration
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.client.JerseyClientConfiguration
import io.dropwizard.testing.junit5.DropwizardClientExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import javax.ws.rs.ProcessingException
import javax.ws.rs.client.Client

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(DropwizardExtensionsSupport::class)
class HttpbinClientTest {

    companion object {
        private val extension = DropwizardClientExtension()
        private val jerseyClientConfiguration = JerseyClientConfiguration()
        private lateinit var client: Client
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            val env = extension.environment
            this.client = JerseyClientBuilder(env).apply {
                using(jerseyClientConfiguration)
            }.build(env.name)
        }
    }

    @RegisterExtension
    private val wiremock = WireMockExtension.newInstance().apply {
        options(wireMockConfig().dynamicPort())
    }.build()

    private val sut: HttpbinClient by lazy {
        val config = AppConfiguration(httpbinBaseUrl = wiremock.baseUrl())
        return@lazy HttpbinClient(config, client)
    }

    @Test
    fun `it should load simple response successfully`(): Unit = runTest {
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

        val response = sut.getSimpleResponse()
        assertThat(response.url).isEqualTo("https://httpbin.org/get")
        assertThat(response.headers.host).isEqualTo("httpbin.org")
    }

    @Test
    fun `it times out after 500ms`(): Unit = runTest {
        wiremock.stubFor(
            get("/get").willReturn(
                ok().withFixedDelay(500)
            )
        )
        assertThat { sut.getSimpleResponse() }
            .isFailure()
            .isInstanceOf(ProcessingException::class.java)
            .hasMessage("java.net.SocketTimeoutException: Read timed out")
    }

    @Test
    fun `it doesn't timeout before 500ms`(): Unit = runTest {
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
                ).withFixedDelay(200)
            )
        )
        val response = sut.getSimpleResponse()
        assertThat(response.url).isEqualTo("https://httpbin.org/get")
        assertThat(response.headers.host).isEqualTo("httpbin.org")
    }
}
