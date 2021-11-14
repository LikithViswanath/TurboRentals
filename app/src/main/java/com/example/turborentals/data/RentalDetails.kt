package com.example.turborentals.data

import com.google.firebase.firestore.DocumentId

data class RentalDetails(
    @DocumentId
    var RentalId : String?=null,

    var RcNumber : String?=null,
    var modelName : String?=null,
    var currentKm : Long?=null,
    var status: String?=null,
    var imageBack : String?=null,
    var imageFront : String?=null,
    var imageLeft : String?=null,
    var imageRight : String?=null,
    var earnings : Long?=0,
    var daysOnRent : Int?=0,
    var updatedKm : Int?=0
)