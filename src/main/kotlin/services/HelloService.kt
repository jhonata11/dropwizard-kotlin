package services

import api.res.HelloResponse
import clients.HttpbinClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelloService @Inject constructor(private val client: HttpbinClient) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    suspend fun getAsyncResponse(): HelloResponse = withContext(Dispatchers.IO) {
        logger.info("Service - STARTED")
        val res1 = async { client.getResponse("request1", 3) }
        val res2 = async { client.getResponse("request2", 1) }
        val res3 = async { client.getResponse("request3", 1) }
        logger.info("Service - END")
        val data = listOf(
            res1.await().args.name,
            res2.await().args.name,
            res3.await().args.name
        )
        return@withContext HelloResponse(data = data)
    }

    suspend fun getBlockingResponse(): HelloResponse = withContext(Dispatchers.IO) {
        logger.info("Service - STARTED")
        val res1 = client.getResponse("request1", 3)
        val res2 = client.getResponse("request2", 1)
        val res3 = client.getResponse("request3", 1)
        logger.info("Service - END")
        val data = listOf(
            res1.args.name,
            res2.args.name,
            res3.args.name
        )
        return@withContext HelloResponse(data = data)
    }
}
