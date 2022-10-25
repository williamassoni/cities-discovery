package com.assoni.discovery.integration

import com.assoni.discovery.cities.dto.CityDTO
import com.assoni.discovery.cities.providers.LocalCityProvider
import com.assoni.discovery.cities.providers.local.persistence.CityRepository
import com.assoni.discovery.cities.providers.local.persistence.entities.CityEntity
import com.assoni.discovery.cities.providers.local.persistence.entities.GeoLocation
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(classes = [LocalCityProviderIntegrationTests.LocalConfigurationOnlyCityEntity::class, LocalCityProvider::class])
@AutoConfigureTestDatabase
@EnableTransactionManagement
@AutoConfigureDataJpa
@Transactional
internal class LocalCityProviderIntegrationTests (@Autowired val cityProvider: LocalCityProvider, @Autowired val cityRepository: CityRepository) {

    @Configuration
    @EnableJpaRepositories(basePackageClasses = [CityRepository::class])
    @EntityScan(basePackageClasses = [CityEntity::class])
    class LocalConfigurationOnlyCityEntity {

    }

    fun initializeStorage() {
        cityRepository.save(CityEntity("Munich", GeoLocation(48.1366,11.5771)))
        cityRepository.save(CityEntity("Berlin", GeoLocation(52.52437,13.41053)))
        cityRepository.save(CityEntity("Hamburg", GeoLocation(53.57532,10.01534)))
    }

    @Test
    fun `should be able to retrieve cities`() {
        initializeStorage()

        var citiesDTO = cityProvider.findAllCities()

        assertThat(citiesDTO).isNotEmpty;
        assertThat(citiesDTO).extracting<String>(CityDTO::name).contains("Munich", "Berlin", "Hamburg")
    }

    @Test
    fun `should be able to retrieve berlin geolocations`() {
        initializeStorage()
        var citiesDTO = cityProvider.findAllCities()

        var berlinAsCity = citiesDTO.find { it.name == "Berlin" }

        assertThat(berlinAsCity).isNotNull;
        berlinAsCity?.let { it ->
            assertThat(it.name).satisfies(Condition({ s -> s.equals("Berlin") }, "cityName"))
            assertThat(it.latitude).isEqualTo(52.52437)
            assertThat(it.longitude).isEqualTo(13.41053)
        }
    }

    @Test
    fun `should not explode when the list of repositories is empty`() {
        var citiesDTO = cityProvider.findAllCities()
        assertThat(citiesDTO).isEmpty()
    }
}
