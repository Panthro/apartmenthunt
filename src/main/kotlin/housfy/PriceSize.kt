package housfy

import com.google.gson.annotations.SerializedName

data class PriceSize(

	@SerializedName("amount") val amount: Int,
	@SerializedName("currency") val currency: String,
)