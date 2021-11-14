package com.example.turborentals

import android.app.Application

class GlobalVariables : Application() {
    var rentalType : String? = null
    var rentalStatus : String? = null
    var bookingStatus : String? = null
    var rcNumber : String? =null
    var startDate : Long?=null
    var endDate : Long? = null
    var phoneNumber : String? =null
    var bookingType : String? = null
}