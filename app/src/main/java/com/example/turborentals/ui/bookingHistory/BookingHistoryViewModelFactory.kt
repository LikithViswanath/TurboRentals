package com.example.turborentals.ui.bookingHistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookingHistoryViewModelFactory( val rentalType : String?, val userId : String, val application: Application ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookingHistoryViewModel::class.java)){
            return BookingHistoryViewModel(rentalType,userId,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}