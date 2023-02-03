package com.example.medhelper.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.medhelper.R
import com.example.medhelper.data.SingleLiveEvent
import com.example.medhelper.domain.AppMessage
import com.example.medhelper.domain.MedInformation
import com.example.medhelper.repository.RepositoryAPI
import com.example.medhelper.settings.DATE_TIME_KEY
import com.example.medhelper.settings.DIASTOLIC_PRESSURE
import com.example.medhelper.settings.HEART_RATE
import com.example.medhelper.settings.SYSTOLIC_PRESSURE
import com.example.medhelper.utils.ZERO
import kotlinx.coroutines.*

class MainViewModel(
    private val application: Application,
    private val repository: RepositoryAPI
) : ViewModel() {
    private val _messagesLiveData = SingleLiveEvent<AppMessage>()
    val messagesLiveData: SingleLiveEvent<AppMessage> by this::_messagesLiveData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        error.printStackTrace()
    }
    private var dbJob: Job? = null
    private val mainScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    private fun isMedInformationCorrect(medInformation: MedInformation) =
        (medInformation.dateTime > Long.ZERO) && (medInformation.systolicPressure > Int.ZERO) &&
                (medInformation.diastolicPressure > Int.ZERO) && (medInformation.heartRate > Int.ZERO)

    fun requestAllData() {
        dbJob?.cancel()
        dbJob = mainScope.launch {
            val collection = repository.getAllInformation()
            val medInfoList = mutableListOf<MedInformation>()
            collection
                .addOnSuccessListener { snapshot ->
                    snapshot.forEach { document ->
                        medInfoList.add(
                            MedInformation(
                                dateTime = document.get(DATE_TIME_KEY) as Long,
                                systolicPressure = (document.get(SYSTOLIC_PRESSURE) as Long).toInt(),
                                diastolicPressure = (document.get(DIASTOLIC_PRESSURE) as Long).toInt(),
                                heartRate = (document.get(HEART_RATE) as Long).toInt()
                            )
                        )
                    }
                    _messagesLiveData.postValue(AppMessage.MedInformationMessage(medInfoList))
                }
                .addOnFailureListener {
                    _messagesLiveData
                        .postValue(AppMessage.InfoSnackBar(application.getString(R.string.message_database_error)))
                }
        }
    }

    fun requestSaveData(medInformation: MedInformation) {
        dbJob?.cancel()
        dbJob = mainScope.launch {
            if (isMedInformationCorrect(medInformation)) {
                repository.saveInformation(medInformation)
                _messagesLiveData
                    .postValue(AppMessage.MedInformationMessage(listOf(medInformation)))
            } else {
                _messagesLiveData
                    .postValue(AppMessage.InfoSnackBar(application.getString(R.string.message_incorrect_data)))
            }
        }
    }

    fun onAddButtonClick() {
        _messagesLiveData.postValue(AppMessage.AddDialog)
    }

}