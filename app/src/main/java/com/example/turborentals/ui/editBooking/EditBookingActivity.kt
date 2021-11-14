package com.example.turborentals.ui.editBooking

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.example.turborentals.databinding.ActivityEditBookingBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class EditBookingActivity : AppCompatActivity() {

    val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType = globalVariables.rentalType
        val phoneNumber = globalVariables.phoneNumber
        val factory = EditBookingViewModelFactory(rentalType,phoneNumber,Firebase.auth.currentUser?.uid.toString(),application)
        ViewModelProvider(this,factory).get(EditBookingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityEditBookingBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_booking)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = EditBookingAdapter()
        binding.selectedRentalRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.selectedRentalRecyclerView.adapter = adapter

        viewModel.datePair.observe(this,{

            val days = "${(((it.endDate!!) - (it.startDate!!)) / (24 * 60 * 60 * 1000))} Days"
            binding.durationDaysTextView.text = days

            viewModel.daysOnRent = (((it.endDate!!) - (it.startDate!!)) / (24 * 60 * 60 * 1000)).toInt()

            val  dates = "${getShortDate(it.startDate)}  ${getShortDate(it.endDate)}"
            binding.fromToDateTextView.text = dates

        })

        viewModel.bookingStatus.observe(this,{
            it?.let {
                binding.bookingStatusChange.text = it
                when(it){
                    "Completed"->{
                        binding.bookingStatusChange.setBackgroundResource(R.color.good_green)
                    }
                    "Upcoming"->{
                        binding.bookingStatusChange.setBackgroundResource(R.color.good_blue)
                        Log.v("Coroutine","background Color is set")
                    }
                    "Running"->{
                        binding.bookingStatusChange.setBackgroundResource(R.color.good_yellow)
                    }
                    "Delivery"->{
                        binding.bookingStatusChange.setBackgroundResource(R.color.good_red)
                    }
                    "Collect"->{
                        binding.bookingStatusChange.setBackgroundResource(R.color.good_pink)
                    }
                }
            }
        })

        viewModel.liveRentalDetails.observe(this,{
            it.let {
                adapter.submitList(it)
            }
        })

    }

    private fun getShortDate(ts:Long?):String{
        if(ts == null) return ""
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = ts
        return android.text.format.DateFormat.format("dd MMM yyyy HH:mm", calendar).toString()
    }



}