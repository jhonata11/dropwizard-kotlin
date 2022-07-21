package health

import kotlinx.coroutines.runBlocking
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck
import services.HelloService
import services.RequestType
import javax.inject.Inject

class AppHealthCheck @Inject constructor(
    private val helloService: HelloService
) : NamedHealthCheck() {
    override fun getName() = "httpbin.org"

    override fun check(): Result {
        return try {
            runBlocking { helloService.getSimpleResponse(RequestType.STATUS, 200) }
            Result.healthy()
        } catch (e: Exception) {
            Result.unhealthy(e.message)
        }
    }
}
