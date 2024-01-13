package com.prajwal.app

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.FetchBirdsDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage

class BirdsViewModel(
    private val fetchBirdsDataUseCase: FetchBirdsDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<BirdsUiState>(BirdsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    fun selectCategory(category: String) {
        println("check category $category")
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    private fun updateImages() {
        viewModelScope.launch {
            try {
                println("check isloading ${uiState.value.isLoading}")
                val images = fetchBirdsDataUseCase()
                _uiState.update {
                    it.copy(images = images, isLoading = false)
                }
                println("check isloading ${uiState.value.isLoading} ${uiState.value.images.size}")
            } catch (ex: Exception) {
                println(ex)
            }
        }
    }

    fun onSelectImage(selectedBird: BirdImage?) {
        _uiState.update {
            it.copy(selectedImage = selectedBird)
        }
    }

    companion object {
        const val BASE_URL = "https://sebi.io/demo-image-api/"
    }
}

data class BirdsUiState(
    val isLoading: Boolean = false,
    val images: List<BirdImage> = emptyList(),
    val selectedCategory: String? = null,
    val selectedImage: BirdImage? = null,
) {
    val categories = images.map { it.category }.toSet()
    val selectedCategoryImages = images.filter { it.category == selectedCategory }
}
