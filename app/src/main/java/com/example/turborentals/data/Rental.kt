package com.example.turborentals.data

import com.google.firebase.firestore.DocumentId

data class Rental(
    @DocumentId
    var rentalID : String? = null ,
    var rental : String? = null,
    var totalRentals : Int? = null,
    var availableRentals : Int? = null,
    var onRentRentals : Int ? = null,
    var bookedRentals : Int ? = null
)