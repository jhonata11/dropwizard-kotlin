package resources

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.HelloService
import services.RequestType
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.ProcessingException
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.container.Suspended
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

enum class Country {
    UK, DE
}

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

fun <T> AsyncResponse.executeAsync(dispatcher: CoroutineDispatcher = Dispatchers.Default, block: suspend () -> T) {
    CoroutineScope(dispatcher).launch {
        try {
            resume(block())
        } catch (ex: ProcessingException) {
            resume(Response.status(408).build())
        } catch (t: Throwable) {
            resume(t)
        }
    }
}