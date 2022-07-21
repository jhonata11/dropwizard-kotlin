package clients.models

import com.fasterxml.jackson.annotation.JsonProperty

data class HttpbinHeaders(
    @JsonProperty("Host") val host: String
)

data class HttpbinResponse(
    @JsonProperty("url") val url: String,
    @JsonProperty("headers") val headers: HttpbinHeaders
)
