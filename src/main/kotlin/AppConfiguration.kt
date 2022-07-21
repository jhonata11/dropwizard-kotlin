import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientConfiguration

class AppConfiguration(
    val appName: String? = "",
    val jerseyClient: JerseyClientConfiguration = JerseyClientConfiguration()
) : Configuration()
