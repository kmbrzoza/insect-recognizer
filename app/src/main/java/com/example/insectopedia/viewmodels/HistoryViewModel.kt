package com.example.insectopedia.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.insectopedia.model.Insect
import com.example.insectopedia.model.InsectopediaDAO
import com.example.insectopedia.model.InsectopediaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: InsectopediaDAO

    init {
        dao = InsectopediaDatabase
            .getInstance(application).dao
    }

    fun delete(insect: Insect) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(insect.photoPath)
            if (file.exists()) {
                file.delete()
            }
            dao.delete(insect.id)
        }
    }

    fun get(id: Long): Insect {
        return dao.get(id)
    }

    val insects: LiveData<List<Insect>> = dao.getAll()
}