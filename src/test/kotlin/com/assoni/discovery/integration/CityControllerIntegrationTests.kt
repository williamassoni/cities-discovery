package com.assoni.discovery.integration

import com.assoni.discovery.cities.CityController
import com.assoni.discovery.cities.CityService
import com.assoni.discovery.cities.dto.CityDTO
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(classes = [CityController::class, CityControllerIntegrationTests.LocalConfiguration::class])
@AutoConfigureCache
@AutoConfigureJson
@AutoConfigureWebFlux
@AutoConfigureWebTestClient
@ImportAutoConfiguration
internal class CityControllerIntegrationTests (@Autowired var webTestClient: WebTestClient) {

    @MockBean
    lateinit var cityService: CityService

    @Configuration
    @EnableAutoConfiguration
    class LocalConfiguration {
    }

    @Test
    fun `should return list of cities`() {
        Mockito.`when`(cityService.findAllCities()).thenReturn(listOf(CityDTO("Berlin", 0.0,0.0)))

        webTestClient.get()
            .uri("/cities")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
                .jsonPath("[0].name").isEqualTo("Berlin")





    }
}
