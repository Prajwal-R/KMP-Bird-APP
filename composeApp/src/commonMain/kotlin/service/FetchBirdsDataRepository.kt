package service

import com.prajwal.app.BirdsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import model.BirdImage

class FetchBirdsDataRepository() {
    private val httpClient =
        HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }

    suspend fun getImages(): List<BirdImage> =
        httpClient
            .get(BirdsViewModel.BASE_URL + "pictures.json")
            .body<List<BirdImage>>()
}
