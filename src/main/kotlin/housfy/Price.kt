package housfy

import com.google.gson.annotations.SerializedName

data class Price(

	@SerializedName("amount") val amount: Int,
	@SerializedName("currency") val currency: String,
)