import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp

const val configPath = "src/integration/resources/test.yml"
@TestDropwizardApp(value = App::class, config = configPath)
class HelloServiceIntegrationTest {
    @Test
    fun `it should call live service`() {
        RestAssured.port = 8080

        When {
            get("/hello")
        } Then {
            body("example", equalTo("https://httpbin.org/get"))
        }
    }
}
