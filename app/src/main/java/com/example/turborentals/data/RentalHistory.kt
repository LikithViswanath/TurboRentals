package com.example.turborentals.data

import com.google.firebase.firestore.DocumentId

data class RentalHistory(
    @DocumentId
    var startDate : String ?=null,
    var RcNumber : String ?= null,
    var modelName : String ?= null,
    var daysOnRent : Int ?= null,
    var currentKm : Long ?=null,
    var updatedKm : Int ?=0,
    var endDate : Long ?= 0
)