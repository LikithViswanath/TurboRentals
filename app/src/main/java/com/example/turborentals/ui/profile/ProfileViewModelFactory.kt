package com.example.turborentals.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileViewModelFactory( val userId : String  ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
           return ProfileViewModel(userId) as T
       }
       else
           throw IllegalArgumentException("Unknown ViewModel class")
    }
}