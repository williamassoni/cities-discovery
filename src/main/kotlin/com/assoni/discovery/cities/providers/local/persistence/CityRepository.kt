package com.assoni.discovery.cities.providers.local.persistence

import com.assoni.discovery.cities.providers.local.persistence.entities.CityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CityRepository : JpaRepository<CityEntity, Long>
