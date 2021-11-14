package com.example.turborentals.ui.rentalsHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RentalsHistoryViewModelFactory( private val rentalType : String ,
                                      private val modelNumber : String ,val userId : String ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RentalsHistoryViewModel::class.java)){
            return RentalsHistoryViewModel(rentalType,modelNumber,userId) as T
        }
        else
            throw IllegalArgumentException("Unknown ViewModel class")
    }

}