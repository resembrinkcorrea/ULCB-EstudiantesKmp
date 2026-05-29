package communicationapp

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient
