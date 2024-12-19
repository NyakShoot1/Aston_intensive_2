package ru.nyakshoot.aston_intensive_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _currentState = MutableLiveData<DrumState>(DrumState.Initial)
    val currentState: LiveData<DrumState> = _currentState

    private val _rotation = MutableLiveData(0f)
    val rotation: LiveData<Float> = _rotation

    fun updateState(type: String) {
        _currentState.value = when (type) {
            "Text" -> DrumState.ShowText
            "Image" -> DrumState.ShowImage
            else -> DrumState.Initial
        }
    }

    fun updateRotation(rotation: Float) {
        _rotation.value = rotation
    }
}

sealed class DrumState {
    data object Initial : DrumState()
    data object ShowText : DrumState()
    data object ShowImage : DrumState()
}