package resources

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.HelloService
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.ProcessingException
import javax.ws.rs.Produces
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
    fun getSimpleResponse(
        @Suspended asyncResponse: AsyncResponse
    ) {
        asyncResponse.executeAsync {
            val res = service.getSimpleResponse()
            return@executeAsync Response.ok(res).build()
        }
    }

    @GET
    @Path("/{statusCode}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getStatusResponse(
        @PathParam("statusCode") statusCode: Int?,
        @Suspended asyncResponse: AsyncResponse
    ) {
        asyncResponse.executeAsync {
            service.getStatusResponse(statusCode ?: 200)
            return@executeAsync Response.ok().build()
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