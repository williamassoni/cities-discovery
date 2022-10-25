package com.assoni.discovery.cities.providers

import com.assoni.discovery.cities.dto.CityDTO
import com.assoni.discovery.cities.providers.local.persistence.CityRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

interface CityProvider {
    fun findAllCities() : List<CityDTO>
}

@Service
@Qualifier("localCityProvider")
class LocalCityProvider(val cityRepository: CityRepository) : CityProvider {

    override fun findAllCities(): List<CityDTO> {
        return cityRepository.findAll().map {
            return@map CityDTO(name=it.name, latitude = it.location.latitude, longitude = it.location.longitude)
        }
    }
}

@Service
@Qualifier("externalCityProvider")
class ExternalCityProvider : CityProvider {

    override fun findAllCities(): List<CityDTO> {
        throw RuntimeException("Not Implemented yet")
    }
}

