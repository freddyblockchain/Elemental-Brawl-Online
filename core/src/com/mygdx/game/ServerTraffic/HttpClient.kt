package com.mygdx.game.ServerTraffic

import io.ktor.client.*
import io.ktor.client.engine.cio.*

val httpClient = HttpClient(CIO) {
    expectSuccess = true
}