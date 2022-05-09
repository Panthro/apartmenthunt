package housfy

import com.google.gson.annotations.SerializedName

data class Sizes(

	@SerializedName("size") val size: Int,
	@SerializedName("sizeBuiltWithoutCommonElements") val sizeBuiltWithoutCommonElements: Int,
	@SerializedName("sizeBuilt") val sizeBuilt: Int,
	@SerializedName("sizeUseful") val sizeUseful: Int,
)