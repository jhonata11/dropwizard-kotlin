import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp

@TestDropwizardApp(value = App::class, config = "src/integration/resources/test.yml")
class HelloServiceIntegrationTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080
        }

        @JvmStatic
        @RegisterExtension
        private val WIREMOCK = WireMockExtension.newInstance().apply {
            options(WireMockConfiguration.wireMockConfig().port(55012))
        }.build()
    }

    @BeforeEach
    fun beforeEach() {
        WIREMOCK.stubFor(
            WireMock.get("/delay/3?name=request1").willReturn(
                WireMock.okJson(
                    """{
                          "args": {
                              "name": "request1"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                )
            )
        )

        WIREMOCK.stubFor(
            WireMock.get("/delay/1?name=request2").willReturn(
                WireMock.okJson(
                    """{
                          "args": {
                              "name": "request2"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                )
            )
        )

        WIREMOCK.stubFor(
            WireMock.get("/delay/1?name=request3").willReturn(
                WireMock.okJson(
                    """{
                          "args": {
                              "name": "request3"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                )
            )
        )
    }

    @Test
    fun `it should execute call successfully`() {
        When {
            get("/api/async")
        } Then {
            body("data", equalTo(listOf("request1", "request2", "request3")))
        }
    }

    @Test
    fun `it should timeout when jersey client times out`() {
        WIREMOCK.stubFor(
            WireMock.get("/delay/3?name=request1").willReturn(
                WireMock.okJson(
                    """{
                          "args": {
                              "name": "request1"
                          },
                          "url": "https://httpbin.org/get"
                        }
                    """.trimIndent()
                ).withFixedDelay(501)
            )
        )

        When {
            get("/api/async")
        } Then {
            statusCode(503)
        }
    }
}
