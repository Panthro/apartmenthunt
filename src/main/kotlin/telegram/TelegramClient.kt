package telegram

import housfy.Property
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST
import java.text.NumberFormat
import java.util.Currency


object TelegramClient {
    private val apiUrl = "https://api.telegram.org/bot"
    private val botToken = System.getenv("TELEGRAM_BOT_TOKEN")
    private val chatId = System.getenv("CHAT_ID").toLong()
    private val numberFormat = NumberFormat.getCurrencyInstance().also { it.currency = Currency.getInstance("EUR") }
    fun notifyNewProperty(property: Property, operation: String) {
        val telegramApi = TelegramApi.build(apiUrl, botToken)
        val message = """
            [${operation.uppercase()}] ${property.providerPropertyTitle}
            💶: ${numberFormat.format(property.price.amount / 100)}
            💶m2: ${numberFormat.format(property.priceSize.amount / 100)}
            📐: ${property.size}m2
            🛏: ${property.numberOfBedrooms}
            🚻: ${property.numberOfBathrooms}
            📍: ${property.neighborhoodLocationName}, ${property.streetName}, ${property.streetNumber}
            ${property.providerPropertyUrl}
        """.trimIndent() as String

        telegramApi.sendMessage(chatId, message).execute()
        telegramApi.sendPhoto(chatId, property.photos.first()).execute()
        telegramApi.sendLocation(chatId, property.latitude, property.longitude).execute()

    }
}


interface TelegramApi {


    companion object {
        fun build(baseUrl: String, botToken: String): TelegramApi =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("$baseUrl$botToken/")
                .build()
                .create(TelegramApi::class.java)
    }

    @POST("sendMessage")
    @FormUrlEncoded
    fun sendMessage(@Field("chat_id") chatId: Long, @Field("text") text: String): Call<Void>

    @POST("sendPhoto")
    @FormUrlEncoded
    fun sendPhoto(@Field("chat_id") chatId: Long, @Field("photo") photoUrl: String): Call<Void>

    @POST("sendLocation")
    @FormUrlEncoded
    fun sendLocation(
        @Field("chat_id") chatId: Long,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
    ): Call<Void>
}