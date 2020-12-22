package com.kotlinflowdemo.ui.fragments.sharedstatedemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class SharedFlowViewModel : ViewModel() {

    private val _timerSharedValue = MutableSharedFlow<Int>(10)
    val timerSharedValue = _timerSharedValue.asSharedFlow()

    init {
        viewModelScope.launch {
            SharedTimer.startTimer()
        }
        startTimer()
    }

    fun startTimer() {
        viewModelScope.launch {

            SharedTimer.timer.collect {
                _timerSharedValue.emit(it)
            }
        }
    }

}