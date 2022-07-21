package resources

import services.HelloService
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/hello")
class HelloResource @Inject constructor(
    private val service: HelloService
) {
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatusResponse(@Suspended asyncResponse: AsyncResponse) {
        asyncResponse.executeAsync {
            val response = service.getSimpleResponse()
            return@executeAsync Response.ok(response).build()
        }
    }
}
