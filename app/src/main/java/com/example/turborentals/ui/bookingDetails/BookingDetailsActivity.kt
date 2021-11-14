package com.example.turborentals.ui.bookingDetails

import android.app.AlarmManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.PhotoActivity
import com.example.turborentals.R
import com.example.turborentals.alarmDisbatcher.AlarmScheduler
import com.example.turborentals.databinding.ActivityBookingDetailsBinding
import com.example.turborentals.ui.bookings.selectedRentals
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BookingDetailsActivity : AppCompatActivity() {

    val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType = globalVariables.rentalType
        val factory = BookingDetailsViewModelFactory(rentalType!!,Firebase.auth.currentUser?.uid.toString(),application)
        ViewModelProvider(this,factory).get(BookingDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val globalVariables =  (application as GlobalVariables)
        viewModel.epochStartDate =  globalVariables.startDate
        viewModel.epochEndDate = globalVariables.endDate
        viewModel.bookingType = globalVariables.bookingType

        val binding : ActivityBookingDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_booking_details)

        val adapter = BookingDetailsAdapter()
        binding.selectedRentalRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.selectedRentalRecyclerView.adapter = adapter
        adapter.submitList(selectedRentals)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val frontIdImagePhoneActivity = PhotoActivity(this)
        frontIdImagePhoneActivity.phoneActivityResultLauncher = registerForActivityResult(frontIdImagePhoneActivity.photoActivityResultContract){
            viewModel.frontIdImageUri.value = it
        }

        val backIdImagePhoneActivity = PhotoActivity(this)
        backIdImagePhoneActivity.phoneActivityResultLauncher = registerForActivityResult(backIdImagePhoneActivity.photoActivityResultContract){
            viewModel.backIdImageUri.value = it
        }

        binding.phoneActivityFront = frontIdImagePhoneActivity
        binding.phoneActivityBack = backIdImagePhoneActivity

        viewModel.frontIdImageUri.observe(this,{
            binding.frontIDImageView.setImageURI(it)
        })

        viewModel.backIdImageUri.observe(this,{
            binding.backIDImageView.setImageURI(it)
        })

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmScheduler = AlarmScheduler(this,alarmManager)

        viewModel.scheduleAnAlarm.observe(this,{

            Log.v("Coroutine","Setting Alarm")

                if(viewModel.bookingType=="OnRent")
                   alarmScheduler.scheduleAnAlarm(it.endDate!!,viewModel.phoneNumber!!,
                    viewModel.rentalType,viewModel.bookingType!!)
                else
                    alarmScheduler.scheduleAnAlarm(it.startDate!!,viewModel.rentalType,
                    viewModel.phoneNumber!!,viewModel.bookingType!!)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        selectedRentals.clear()
    }
}