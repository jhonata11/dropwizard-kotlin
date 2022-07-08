import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import ru.vyarus.dropwizard.guice.GuiceBundle

class App : Application<AppConfiguration>() {
    override fun run(config: AppConfiguration, env: Environment) {
        // TODO: check what to do here.
    }

    override fun initialize(bootstrap: Bootstrap<AppConfiguration>) {
        val guiceBundle = GuiceBundle.builder().apply {
            enableAutoConfig("resources", "health")
            modules(AppModule())
        }.build()
        bootstrap.addBundle(guiceBundle)
    }
}