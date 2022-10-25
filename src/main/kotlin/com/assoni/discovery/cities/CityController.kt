package com.assoni.discovery.cities

import com.assoni.discovery.cities.dto.CityDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/cities"])
class CityController(@Autowired val cityService: CityServiceIT) {

    @GetMapping
    fun findAllCities():List<CityDTO> {
        return cityService.findAllCities();
    }
}
