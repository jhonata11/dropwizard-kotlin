package resources

import jakarta.ws.rs.ProcessingException
import jakarta.ws.rs.container.AsyncResponse
import jakarta.ws.rs.core.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

fun <T> AsyncResponse.executeAsync(dispatcher: CoroutineDispatcher = Dispatchers.Default, block: suspend () -> T) {
    CoroutineScope(dispatcher).launch {
        try {
            resume(block())
        } catch (ex: ProcessingException) {
            val res = when (ex.cause?.cause) {
                is SocketTimeoutException -> Response.status(503).build()
                else -> Response.status(500).build()
            }
            resume(res)
        } catch (t: Throwable) {
            resume(t)
        }
    }
}
