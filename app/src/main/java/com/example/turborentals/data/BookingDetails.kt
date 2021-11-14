package com.example.turborentals.data

import com.google.firebase.firestore.DocumentId

data class BookingDetails(

    @DocumentId
    var phoneNumber: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var rentalsIncluded: MutableList<RentalDetails>? = null,
    var fullName: String? = null,
    var phoneOptional: Long? = null,
    var address: String? = null,
    var totalAmount: Long? = null,
    var securityAmount: Long? = null,
    var paidAmount: Long? = null,
    var balanceAmount: Long? = null,
    var balanceAmountCollected: Long? = null,
    var fineCollected: Long? = null,
    var securityRefunded: Long? = null,
    var frontIdImage: String? = null,
    var backIdImage: String? = null,
    var bookingStatus : String? = null
)