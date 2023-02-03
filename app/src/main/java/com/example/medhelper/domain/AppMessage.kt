package com.example.medhelper.domain

sealed class AppMessage {
    data class MedInformationMessage(val medInformation: List<MedInformation>) : AppMessage()
    data class InfoSnackBar(val text: String) : AppMessage()
    data class InfoToast(val text: String, val length: Int) : AppMessage()
    object AddDialog : AppMessage()
}