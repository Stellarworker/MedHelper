package com.example.medhelper.repository

import com.example.medhelper.domain.MedInformation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface RepositoryAPI {
    fun getAllInformation(): Task<QuerySnapshot>
    fun saveInformation(medInformation: MedInformation)
}