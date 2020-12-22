package com.kotlinflowdemo.ui.fragments.sharedstatedemo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


object SharedTimer {

    private val _timer = MutableSharedFlow<Int>(10)
    val timer = _timer.asSharedFlow()

    suspend fun startTimer() {
        for (counter in 0..100) {
            delay(1000)
            _timer.emit(counter)
        }
    }
}