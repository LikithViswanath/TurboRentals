package com.example.turborentals.ui.bookingDetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BookingDetailsViewModelFactory( val rentalType : String,val userId : String ,val application: Application)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookingDetailsViewModel::class.java)){
            return BookingDetailsViewModel(rentalType,userId,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}