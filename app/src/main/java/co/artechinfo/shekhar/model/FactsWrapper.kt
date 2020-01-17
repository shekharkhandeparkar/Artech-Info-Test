package co.artechinfo.shekhar.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
* FactsWrapper class
* wrapper class for data from the api
* */
@JsonClass(generateAdapter = true)
data class FactsWrapper(
    @Json(name = "title")
    var title: String? = null,

    @Json(name = "rows")
    var rows: List<Fact>? = null
)