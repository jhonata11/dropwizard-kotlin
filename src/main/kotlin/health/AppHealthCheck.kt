package health

import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck

class AppHealthCheck: NamedHealthCheck(){
    override fun check(): Result {
        return Result.healthy();
    }

    override fun getName() = "some health check"
}