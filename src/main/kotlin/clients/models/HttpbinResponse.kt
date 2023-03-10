package clients.models

import com.fasterxml.jackson.annotation.JsonProperty

data class HttpBinArgs(
    @JsonProperty("name") val name: String
)

data class HttpbinResponse(
    @JsonProperty("url") val url: String,
    @JsonProperty("args") val args: HttpBinArgs
)
