package com.example.medhelper.repository

import com.example.medhelper.domain.MedInformation
import com.example.medhelper.settings.DATE_TIME_KEY
import com.example.medhelper.settings.DIASTOLIC_PRESSURE
import com.example.medhelper.settings.HEART_RATE
import com.example.medhelper.settings.SYSTOLIC_PRESSURE
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class RepositoryAPIImpl : RepositoryAPI {

    private var myDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var medInfoBase: CollectionReference = myDB.collection(COLLECTION)

    override fun getAllInformation() =
        myDB.collection(COLLECTION).orderBy(DATE_TIME_KEY).get()


    override fun saveInformation(medInformation: MedInformation) {
        medInfoBase.add(
            mapOf(
                DATE_TIME_KEY to medInformation.dateTime,
                SYSTOLIC_PRESSURE to medInformation.systolicPressure,
                DIASTOLIC_PRESSURE to medInformation.diastolicPressure,
                HEART_RATE to medInformation.heartRate
            )
        )
    }

    companion object {
        private const val COLLECTION = "med_info_base"
    }

}