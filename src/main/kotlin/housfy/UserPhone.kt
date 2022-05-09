package housfy

import com.google.gson.annotations.SerializedName

data class UserPhone(

	@SerializedName("countryCode") val countryCode: String,
	@SerializedName("phone") val phone: String,
	@SerializedName("internationalPhone") val internationalPhone: String,
)