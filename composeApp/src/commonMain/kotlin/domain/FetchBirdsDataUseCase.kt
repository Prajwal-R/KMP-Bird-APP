package domain

import service.FetchBirdsDataRepository

class FetchBirdsDataUseCase(
    private val fetchBirdsDataRepository: FetchBirdsDataRepository,
) {
    suspend operator fun invoke() = fetchBirdsDataRepository.getImages()
}
