package com.kotlinflowdemo.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotlinflowdemo.data.theme.Theme
import com.kotlinflowdemo.data.theme.ThemeDataSource

class MainViewModel @ViewModelInject constructor(
    private val themeDataSource: ThemeDataSource
) : ViewModel() {

    // Whenever there is a change in theme, it will be
    // converted to live data
    private val _theme: LiveData<Theme> = themeDataSource
        .getTheme()
        .asLiveData(viewModelScope.coroutineContext)

    val theme: LiveData<Theme>
        get() = _theme

    fun setTheme(theme: Theme) {
        themeDataSource.setTheme(theme)
    }

}