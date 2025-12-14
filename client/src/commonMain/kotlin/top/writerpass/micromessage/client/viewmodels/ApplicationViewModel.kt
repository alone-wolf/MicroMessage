package top.writerpass.micromessage.client.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ApplicationViewModel : ViewModel() {
    var showMainWindow by mutableStateOf(true)
    var pinMainWindowOnTop by mutableStateOf(false)
}