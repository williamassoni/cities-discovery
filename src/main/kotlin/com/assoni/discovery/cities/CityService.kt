package com.assoni.discovery.cities

import com.assoni.discovery.cities.dto.CityDTO
import com.assoni.discovery.cities.providers.CityProvider
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

interface CityServiceIT {
    fun findAllCities():List<CityDTO>
}

@Service
class CityService(
        @Qualifier("localCityProvider") @Autowired private val localCityProvider: CityProvider,
        @Qualifier("externalCityProvider") @Autowired private val externalCityProvider: CityProvider) : CityServiceIT {

    private val logger = KotlinLogging.logger {}

    @CircuitBreaker(name = "findAllCitiesFallBack", fallbackMethod = "findAllLocalRepositories")
    override fun findAllCities() : List<CityDTO> {
        return externalCityProvider.findAllCities()
    }

    fun findAllLocalRepositories(failureError: Exception): List<CityDTO?> {
        logger.error(failureError) { "The external service exploded falling back to in memory storage $failureError" }

        return localCityProvider.findAllCities()
    }
}
