package com.assoni.discovery.cities.providers.local.persistence.entities

data class GeoLocation(val latitude: Double, val longitude: Double) {

    //need it for jpa
    constructor() : this(Double.MIN_VALUE, Double.MIN_VALUE)
}
