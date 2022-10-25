package com.assoni.discovery.cities.providers.local.persistence.entities

import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class CityEntity(
    @Id @GeneratedValue
    val id: Long?,
    var name: String,
    @Embedded
    var location: GeoLocation
) {
    constructor(name: String, location: GeoLocation) : this(null, name, location)
}
