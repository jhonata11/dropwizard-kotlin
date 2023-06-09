package health

import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck
import services.HelloService

class AppHealthCheck @Inject constructor(
    private val helloService: HelloService
) : NamedHealthCheck() {
    override fun getName() = "httpbin.org"

    override fun check(): Result {
        return try {
            runBlocking { helloService.getAsyncResponse() }
            Result.healthy()
        } catch (e: Exception) {
            Result.unhealthy(e.message)
        }
    }
}
