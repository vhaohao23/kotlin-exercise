package com.example.bluromatic.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.bluromatic.BluromaticApplication
import com.example.bluromatic.data.BluromaticRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: Uri) : BlurUiState
    object Error : BlurUiState
}

class BlurViewModel(private val bluromaticRepository: BluromaticRepository) : ViewModel() {

    internal val blurUiState: StateFlow<BlurUiState> = bluromaticRepository.outputWorkInfo
        .map { info ->
            val outputImageUri = info.outputData.getString(com.example.bluromatic.data.KEY_IMAGE_URI)
            when {
                info.state.isFinished && outputImageUri?.isNotEmpty() == true -> {
                    BlurUiState.Complete(Uri.parse(outputImageUri))
                }
                info.state == WorkInfo.State.CANCELLED -> BlurUiState.Default
                info.state == WorkInfo.State.FAILED -> BlurUiState.Error
                else -> BlurUiState.Loading
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, BlurUiState.Default)

    fun applyBlur(blurLevel: Int) = bluromaticRepository.applyBlur(blurLevel)

    fun cancelWork() = bluromaticRepository.cancelWork()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bluromaticRepository = (this[APPLICATION_KEY] as BluromaticApplication).container.bluromaticRepository
                BlurViewModel(bluromaticRepository)
            }
        }
    }
}
