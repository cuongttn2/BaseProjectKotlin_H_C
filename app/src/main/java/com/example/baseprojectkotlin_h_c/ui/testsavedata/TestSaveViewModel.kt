package com.example.baseprojectkotlin_h_c.ui.testsavedata

import android.app.Application
import androidx.lifecycle.*
import com.example.baseprojectkotlin_h_c.ui.testsavedata.model.TimeModel
import com.example.baseprojectkotlin_h_c.utils.SharedPreferenceUtils

class TestSaveViewModel(val app: Application) : ViewModel() {
    private val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(app)
    private val dataInfoLiveData = MutableLiveData<TimeModel?>(null)

    fun saveData() {
        val timeModel = TimeModel("Hi", System.currentTimeMillis())
        sharedPreferenceUtils.setObjModel(timeModel)
        dataInfoLiveData.value = timeModel
    }

    fun getTimeModel() {
        dataInfoLiveData.value = sharedPreferenceUtils.getObjModel<TimeModel>()
    }

    val timeStringLiveDate: LiveData<String> = Transformations.map(dataInfoLiveData) {
        if (it == null) {
            "Data is null"
        } else {
            "Data is (${it.title} \n ${it.time})"
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TestSaveViewModel::class.java)) {
                return TestSaveViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}