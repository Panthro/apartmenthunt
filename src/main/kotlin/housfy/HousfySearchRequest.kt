package housfy

import com.google.gson.annotations.SerializedName

data class HousfySearchRequest(

    @SerializedName("isSold") val isSold: Boolean = false,
    @SerializedName("page") val page: Int = 1,
    @SerializedName("itemsPerPage") val itemsPerPage: Int = 20,
    @SerializedName("order") val order: String = "relevance",
    @SerializedName("providerCode") val providerCode: String = "housfy",
    @SerializedName("mode") val mode: String = "listing",
    @SerializedName("countryIsoCode") val countryIsoCode: String = "ES",
    @SerializedName("operationTypeCode") val operationTypeCode: String = "sale",
    @SerializedName("nearbyFallback") val nearbyFallback: Boolean = true,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("radius") val radius: Int,
)