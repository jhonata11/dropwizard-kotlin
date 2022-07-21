package services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.ws.rs.ProcessingException
import javax.ws.rs.container.AsyncResponse
import javax.ws.rs.core.Response

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
