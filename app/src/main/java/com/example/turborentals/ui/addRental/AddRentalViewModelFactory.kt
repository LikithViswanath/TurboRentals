package com.example.turborentals.ui.addRental

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turborentals.ui.allRentals.AllRentalsViewModel

class AddRentalViewModelFactory( private val rentalType : String?,private val application: Application,val userId : String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRentalViewModel::class.java)) {
            return AddRentalViewModel( rentalType,application,userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}