package clients

import AppConfiguration
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.client.JerseyClientConfiguration
import io.dropwizard.testing.junit5.DropwizardClientExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExtendWith(DropwizardExtensionsSupport::class)
class HttpbinClientTest {

    companion object {
        val EXT = DropwizardClientExtension()
    }

    @RegisterExtension
    private val wiremock = WireMockExtension.newInstance().apply {
        options(wireMockConfig().dynamicPort())
    }.build()

    private val sut: HttpbinClient by lazy {
        val env = EXT.environment
        val client = JerseyClientBuilder(env).apply {
            using(JerseyClientConfiguration())
        }.build(env.name)
        val config = AppConfiguration(httpbinBaseUrl = wiremock.baseUrl())
        return@lazy HttpbinClient(config, client)
    }

    @Test
    fun `it should load simple response successfully`(): Unit = runBlocking {
        wiremock.stubFor(
            get("/get").willReturn(
                okJson(
                    """{
                          "args": {}, 
                          "headers": {
                            "Host": "httpbin.org"
                          }, 
                          "origin": "82.129.110.49", 
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
}
