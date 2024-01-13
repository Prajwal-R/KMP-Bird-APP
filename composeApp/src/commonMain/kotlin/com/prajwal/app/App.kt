package com.prajwal.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.prajwal.app.BirdsViewModel.Companion.BASE_URL
import di.ViewModelDependencies.getBirdsViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage

@Composable
internal fun App() {
    BirdTheme {
        BirdsPage()
    }
}

@Composable
fun BirdTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme =
        MaterialTheme.colorScheme.copy(
            primary = Color.Black,
        ),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirdsPage(viewModel: BirdsViewModel = getBirdsViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(uiState.isLoading) {
            AlertDialog(
                modifier = Modifier.size(100.dp),
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                ),
                onDismissRequest = {},
            ) {
                Text("Loading...")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Top,
        ) {
            for (category in uiState.categories) {
                Button(
                    modifier = Modifier.height(32.dp).fillMaxSize().weight(1f),
                    onClick = {
                        viewModel.selectCategory(category)
                    },
                    shape = RoundedCornerShape(16.dp),
                    elevation =
                    ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        focusedElevation = 4.dp,
                        pressedElevation = 8.dp,
                    ),
                ) {
                    Text(category)
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        AnimatedVisibility(uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                content = {
                    if (uiState.selectedCategoryImages.isNotEmpty()) {
                        items(uiState.selectedCategoryImages) {
                            BirdImageCell(
                                image = it,
                                isSelected = it == uiState.selectedImage,
                                onAction = { selectedImage ->
                                    if (uiState.selectedImage != selectedImage) {
                                        viewModel.onSelectImage(selectedImage)
                                    } else {
                                        viewModel.onSelectImage(null)
                                    }
                                },
                            )
                        }
                    } else {
                        items(uiState.images) {
                            BirdImageCell(
                                image = it,
                                isSelected = it == uiState.selectedImage,
                                onAction = { selectedImage -> viewModel.onSelectImage(selectedImage) },
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun BirdImageCell(
    image: BirdImage,
    isSelected: Boolean = false,
    onAction: (BirdImage) -> Unit,
) {
    Column(
        modifier =
        Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = {
                onAction(image)
            },
        ),
    ) {
        KamelImage(
            resource = asyncPainterResource(data = BASE_URL + image.path),
            contentDescription = "${image.category} by ${image.author}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).clip(RoundedCornerShape(16.dp)),
        )
        AnimatedVisibility(isSelected) {
            Column {
                Spacer(Modifier.size(2.dp))
                Text("Bird Category is " + image.category)
                Spacer(Modifier.size(2.dp))
                Text("Bird Author is" + image.author)
            }
        }
    }
}
