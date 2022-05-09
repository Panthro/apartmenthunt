package housfy

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST

interface HousfyApiClient {

    @POST("/api/public/v1/properties")
    fun searchProperties(@Body request: HousfySearchRequest): Call<HousfySearchApiResponse>


    companion object {
        fun build(): HousfyApiClient = Retrofit.Builder()
            .baseUrl("https://api.housfy.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

}