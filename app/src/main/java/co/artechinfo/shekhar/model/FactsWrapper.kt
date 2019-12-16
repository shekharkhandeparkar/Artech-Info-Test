package co.artechinfo.shekhar.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FactsWrapper(
    @Json(name = "title")
    var title: String? = null,

    @Json(name = "rows")
    var rows: MutableList<Fact>? = null
)