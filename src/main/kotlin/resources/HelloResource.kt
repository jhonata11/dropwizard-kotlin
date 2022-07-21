package resources

import api.req.RequestType
import services.HelloService
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
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
    fun getStatusResponse(
        @NotBlank @QueryParam("payload") payload: String?,
        @NotBlank @QueryParam("type") type: String,
        @Suspended asyncResponse: AsyncResponse
    ) {
        val requestType = RequestType.valueOf(type)
        val parsedPayload = payload?.toIntOrNull()
        asyncResponse.executeAsync {
            val response = service.getSimpleResponse(requestType, parsedPayload)
            return@executeAsync Response.ok(response).build()
        }
    }
}
