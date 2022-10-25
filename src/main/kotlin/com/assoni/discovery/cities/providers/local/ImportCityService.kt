package com.assoni.discovery.cities.providers.local

import com.assoni.discovery.cities.providers.local.persistence.CityRepository
import com.assoni.discovery.cities.providers.local.persistence.entities.CityEntity
import com.assoni.discovery.cities.providers.local.persistence.entities.GeoLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.nio.file.Paths
import kotlin.io.path.bufferedReader

@Service
class ImportCityService(private val cityRepository: CityRepository) : ApplicationRunner {
    object CSVInfo {
        const val CSV_CITY_NAME_COLUMN = 7
        const val CSV_CITY_LONG_COLUMN = 14
        const val CSV_CITY_LAT_COLUMN = 15
    }

    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {
        runBlocking {
            launch {
                delay(6000L)
                importCitiesFromFile()
            }

            logger.info { "Schedule in memory-db import" }
        }
    }

    fun importCitiesFromFile() {
        logger.info { "Initializing city geolocation db" }
        val csvParser = CSVParser(Paths.get(ClassLoader.getSystemResource("files/AuszugGV3QAktuell.csv").toURI()).bufferedReader(), CSVFormat.DEFAULT);
        for (csvRecord in csvParser) {
            var cityName = csvRecord.get(CSVInfo.CSV_CITY_NAME_COLUMN);
            var long = csvRecord.get(CSVInfo.CSV_CITY_LONG_COLUMN);
            var lat = csvRecord.get(CSVInfo.CSV_CITY_LAT_COLUMN);

            var savedEntity =
            notNullNorEmpty(lat, long) { lat, long ->
                var location = GeoLocation(convertValueToDouble(lat), convertValueToDouble(long))
                cityRepository.save(CityEntity(cityName, location))
            }

            savedEntity?.let {
                logger.info { "entity ${it.id} saved" }
            }
        }

        logger.info { "Total of ${cityRepository.findAll().count()}" }
    }

    private inline fun <T1 : String, T2 : String, R : Any> notNullNorEmpty(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
        return if ((p1 != null && p2 != null) && (StringUtils.hasText(p1)) && (StringUtils.hasText(p2)) ) block(p1, p2) else null
    }

    private inline fun convertValueToDouble(valueAsString:String): Double {
        return valueAsString.replace(",",".").toDouble()
    }
}
