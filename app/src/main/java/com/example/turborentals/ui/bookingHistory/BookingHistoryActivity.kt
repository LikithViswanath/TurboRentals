package com.example.turborentals.ui.bookingHistory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.example.turborentals.databinding.ActivityBookingHistoryBinding
import com.example.turborentals.ui.editBooking.EditBookingActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BookingHistoryActivity : AppCompatActivity() {

    val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType = globalVariables.rentalType
        val factory = BookingHistoryViewModelFactory(rentalType,Firebase.auth.currentUser?.uid.toString(),application)
        ViewModelProvider(this,factory).get(BookingHistoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityBookingHistoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_booking_history)
        val adapter = BookingHistoryParentAdapter()
        binding.bookingHistoryParentRecycler.adapter = adapter

        binding.viewModel = viewModel

        val bookingDetailsClickListener= BookingDetailsClickListener {
            val intent = Intent(this,EditBookingActivity::class.java)
            val globalVariables =  (application as GlobalVariables)
            globalVariables.phoneNumber = it.phoneNumber
            startActivity(intent)
        }

        adapter.bookingDetailsClickListener = bookingDetailsClickListener

        viewModel.liveRentalDetails.observe(this,{
            it.let {
                adapter.submitList(it)
            }
        })

    }
}