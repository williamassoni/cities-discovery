package com.assoni.discovery.integration

import com.assoni.discovery.cities.CityService
import com.assoni.discovery.cities.dto.CityDTO
import com.assoni.discovery.cities.providers.ExternalCityProvider
import com.assoni.discovery.cities.providers.LocalCityProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

@SpringBootTest(classes = [CityService::class, CityServiceIntegrationTests.LocalTestConfiguration::class])
internal class CityServiceIntegrationTests (@Autowired val cityService: CityService) {
    @MockBean lateinit var localCityProvider: LocalCityProvider
    @MockBean lateinit var externalCityProvider: ExternalCityProvider

    @Configuration
    @EnableAutoConfiguration
    class LocalTestConfiguration {

    }

    @Test
    fun `should use external provider whenever is available`() {
        Mockito.`when`(externalCityProvider.findAllCities()).thenReturn(listOf(CityDTO("Berlin",0.0,0.0)))

        val cities = cityService.findAllCities()
        Assertions.assertThat(cities).isNotEmpty;
        Assertions.assertThat(cities).contains(CityDTO("Berlin",0.0,0.0))
    }

    @Test
    fun `should fall back to local storage whenever the external provider has issues`() {
        Mockito.`when`(localCityProvider.findAllCities()).thenReturn(listOf(CityDTO("Berlin",0.0,0.0)))
        Mockito.`when`(externalCityProvider.findAllCities()).thenThrow(RuntimeException("Everything is broken"))

        val cities = cityService.findAllCities()
        Assertions.assertThat(cities).isNotEmpty;
        Assertions.assertThat(cities).contains(CityDTO("Berlin",0.0,0.0))
    }
}
