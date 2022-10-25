package com.assoni.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CitiesDiscoveryApplication

fun main(args: Array<String>) {
	runApplication<CitiesDiscoveryApplication>(*args)
}
