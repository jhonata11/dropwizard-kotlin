import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.setup.Environment
import javax.ws.rs.client.Client

class AppModule : AbstractModule() {
    override fun configure() {
        // Add your config
    }

    @Provides
    @Singleton
    fun getHttpClient(config: AppConfiguration, env: Environment): Client {
        return JerseyClientBuilder(env).using(config.jerseyClient).build(env.name)
    }
}
