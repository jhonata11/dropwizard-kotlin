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

@Path("/api")
class HelloResource @Inject constructor(
    private val service: HelloService
) {
    @GET
    @Path("/async")
    @Produces(MediaType.APPLICATION_JSON)
    fun getAsyncResponse(@Suspended asyncResponse: AsyncResponse) {
        asyncResponse.executeAsync {
            val response = service.getAsyncResponse()
            return@executeAsync Response.ok(response).build()
        }
    }

    @GET
    @Path("/blocking")
    @Produces(MediaType.APPLICATION_JSON)
    fun getBlockingResponse(@Suspended asyncResponse: AsyncResponse) {
        asyncResponse.executeAsync {
            val response = service.getBlockingResponse()
            return@executeAsync Response.ok(response).build()
        }
    }
}
