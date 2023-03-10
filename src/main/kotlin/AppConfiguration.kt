import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration

data class AppConfiguration(
    val appName: String? = "",
    val httpbinBaseUrl: String? = "",
    val jerseyClient: JerseyClientConfiguration? = JerseyClientConfiguration()
) : Configuration()
