package com.example.turborentals.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.example.turborentals.ui.addRental.AddRentalActivity
import com.example.turborentals.ui.allRentals.AllRentalsActivity
import com.example.turborentals.ui.bookingHistory.BookingHistoryActivity
import com.example.turborentals.ui.bookings.BookingActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val profileViewModel by lazy {
        val factory = ProfileViewModelFactory(Firebase.auth.currentUser?.uid.toString())
        ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProfileAdapter()
        profileRecyclerView.adapter  = adapter

        val globalVariables =  (application as GlobalVariables)

        adapter.addButtonListener = ProfileAdapter.AddButtonListener {
            val intent = Intent(this,AddRentalActivity::class.java)
            globalVariables.rentalType = it.rentalID
            startActivity(intent)
        }

        adapter.bookingsButtonListener = ProfileAdapter.BookingsButtonListener {
            val intent = Intent(this,BookingHistoryActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.bookingStatus = "All"
            startActivity(intent)
        }

        adapter.bookButtonListener = ProfileAdapter.BookButtonListener {
            val intent = Intent(this,BookingActivity::class.java)
            globalVariables.rentalType = it.rentalID
            startActivity(intent)
        }

        adapter.collectButtonListener = ProfileAdapter.CollectButtonListener {
            val intent = Intent(this,BookingHistoryActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.bookingStatus = "Collect"
            startActivity(intent)
        }

        adapter.todayListener = ProfileAdapter.TodayListener{
            val intent = Intent(this,BookingHistoryActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.bookingStatus = "Today"
            startActivity(intent)
        }

        /*----------------------------------------------------------------------------------*/

        adapter.totalLayoutListener = ProfileAdapter.TotalLayoutListener{
            val intent = Intent(this,AllRentalsActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.rentalStatus = "All"
            startActivity(intent)
        }

        adapter.onAvailableListener = ProfileAdapter.OnAvailableListener{
            val intent = Intent(this,AllRentalsActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.rentalStatus = "Available"
            startActivity(intent)
        }

        adapter.onBookedListener = ProfileAdapter.OnBookedListener{
            val intent = Intent(this,AllRentalsActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.rentalStatus = "Booked"
            startActivity(intent)
        }

        adapter.onRentListener = ProfileAdapter.OnRentListener{
            val intent = Intent(this,AllRentalsActivity::class.java)
            globalVariables.rentalType = it.rentalID
            globalVariables.rentalStatus = "OnRent"
            startActivity(intent)
        }

        /*----------------------------------------------------------------------------------*/

        profileProgressBar.visibility = View.VISIBLE
        profileViewModel.rentalData.observe(this, {
            adapter.submitList(it)
            profileProgressBar.visibility  = View.GONE
        })

    }
}