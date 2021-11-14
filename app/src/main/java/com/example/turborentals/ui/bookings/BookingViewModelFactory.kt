package com.example.turborentals.ui.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turborentals.ui.bookingHistory.BookingHistoryViewModel

class BookingViewModelFactory( private val rentalType : String?,private  val  userId : String ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(BookingViewModel::class.java) ){
            return BookingViewModel(rentalType,userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}