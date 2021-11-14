package com.example.turborentals.ui.editBooking

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditBookingViewModelFactory( val rentalType : String? , val phoneNumber : String?,val userId : String,val application: Application ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditBookingViewModel::class.java)){
               return EditBookingViewModel(rentalType!!,phoneNumber!!,userId,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}