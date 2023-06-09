package resources

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.container.AsyncResponse
import jakarta.ws.rs.container.Suspended
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import services.HelloService

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
