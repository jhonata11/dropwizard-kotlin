import io.dropwizard.client.JerseyClientConfiguration
import io.dropwizard.core.Configuration

data class AppConfiguration(
    val appName: String? = "",
    val httpbinBaseUrl: String? = "",
    val jerseyClient: JerseyClientConfiguration? = JerseyClientConfiguration()
) : Configuration()
