package com.example.insectopedia.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.insectopedia.viewmodels.FindInsectViewModel

class FindInsectViewModelFactory(
    private val application: Application,
    private val imagePath: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindInsectViewModel::class.java)) {
            return FindInsectViewModel(application, imagePath) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
