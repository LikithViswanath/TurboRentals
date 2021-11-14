package com.example.turborentals.ui.allRentals

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.turborentals.ui.addRental.AddRentalViewModel

class AllRentalsViewModelFactory( private val rentalType : String,private val userId : String,private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllRentalsViewModel::class.java)) {
            return AllRentalsViewModel( rentalType,userId,application ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}