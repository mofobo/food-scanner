package ch.mofobo.foodscanner.features.scanner.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraScannerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is fight Fragment"
    }
    val text: LiveData<String> = _text
}