package com.example.medhelper.domain

import com.example.medhelper.utils.ZERO

data class MedInformation(
    var dateTime: Long = Long.ZERO,
    val systolicPressure: Int = Int.ZERO,
    val diastolicPressure: Int = Int.ZERO,
    val heartRate: Int = Int.ZERO
)