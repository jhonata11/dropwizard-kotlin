import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp

@TestDropwizardApp(value = App::class, config = "src/integration/resources/test.yml")
class HelloServiceIntegrationTest {

    @RegisterExtension
    private val wiremock = WireMockExtension.newInstance().apply {
        options(WireMockConfiguration.wireMockConfig().port(55012))
    }.build()

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeClass() {
            RestAssured.port = 8080
        }
    }

    @Test
    fun `it should execute call successfully`() {
        wiremock.stubFor(
            WireMock.get("/get").willReturn(
                WireMock.okJson(
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

        When {
            get("/hello")
        } Then {
            body("example", equalTo("https://httpbin.org/get"))
        }
    }

    @Test
    fun `it should timeout after deadline`() {
        wiremock.stubFor(
            WireMock.get("/get").willReturn(
                WireMock.okJson(
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

        When {
            get("/hello")
        } Then {
            statusCode(503)
        }
    }
}
