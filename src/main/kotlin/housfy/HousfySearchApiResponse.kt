package housfy

import com.google.gson.annotations.SerializedName

data class HousfySearchApiResponse(

	@SerializedName("items") val items: List<Property>,
	@SerializedName("nearbyItems") val nearbyItems: List<String>,
	@SerializedName("page") val page: Int,
	@SerializedName("itemsPerPage") val itemsPerPage: Int,
	@SerializedName("totalItems") val totalItems: Int,
	@SerializedName("totalPages") val totalPages: Int,
)