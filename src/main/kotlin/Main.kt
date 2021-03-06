import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import housfy.HousfyApiClient
import housfy.HousfySearchRequest
import housfy.Property
import telegram.TelegramClient
import java.io.BufferedReader
import java.io.FileWriter
import java.nio.file.Path
import java.util.logging.Logger
import kotlin.io.path.bufferedReader
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.io.path.writer
import kotlin.system.exitProcess

fun main() {
    val maxPrice = 35000000
    val city = "Barcelona"
    val latitude = 41.401291
    val longitude = 2.197971
    val radius = 2000
    val itemsPerPage = 1000
    val operationTypeCode = arrayOf("sale", "rental")
    val apiClient = HousfyApiClient.build()

    val logger = Logger.getLogger("MAIN")

    operationTypeCode.associateWith { operation ->

        val request = HousfySearchRequest(
            latitude = latitude,
            longitude = longitude,
            radius = radius,
            itemsPerPage = itemsPerPage,
            operationTypeCode = operation
        )
        logger.info {
            "process=fetch-properties operation=$operation request=$request"
        }

        val response = apiClient.searchProperties(request).execute()
        logger.info {
            "process=fetch-properties operation=$operation httpStatus=${response.code()}"
        }

        val apiResponse = response.body()!!.items.filter { it.cityLocationName == "Barcelona" }
            .filterNot { it.isSold }
            .filter { it.price.amount <= maxPrice }
            .filter { it.cityLocationName == city }

        logger.info {
            "process=fetch-properties operation=$operation filteredResponseSize=${apiResponse.size}"
        }

        apiResponse.associateBy { it.uuid }

    }.forEach { (operation, properties) ->
        val knownProperties: Map<String, Property> = PropertyRepository.load(operation).ifEmpty {
            logger.info {
                "process=fetch-properties status=first-run-save operation=$operation"
            }
            PropertyRepository.save(properties, operation)
            properties
        }
        properties.forEach { (uuid, property) ->
            if (!knownProperties.containsKey(uuid)) {
                logger.info {
                    "process=fetch-properties status=new-property operation=$operation url=${property.providerPropertyUrl}"
                }
                TelegramClient.notifyNewProperty(property,operation)
            }
        }

        PropertyRepository.save(properties, operation)

    }

    exitProcess(0)

}

object PropertyRepository {

    private val outputDirectory = Path.of("output").also {
        if(it.notExists()) it.createDirectory()
    }

    fun load(operation: String): Map<String, Property> =
        outputDirectory.resolve("$operation.json")
            .also { if (!it.exists()) it.createFile() }
            .bufferedReader().let<BufferedReader, Map<String, Property>?> {
                with(it) {
                    gson.fromJson(this, object : TypeToken<Map<String, Property>>() {}.type)
                }
            } ?: mapOf()

    fun save(properties: Map<String, Property>, operation: String) {
        outputDirectory.resolve("$operation.json")
            .also { if (!it.exists()) it.createFile() }
            .writer()
            .also {
                with(it) {
                    gson
                        .toJson(properties, this)
                }
            }.flush()
    }

    private val gson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}

