package com.example.turborentals.ui.bookings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.ActivityBookingBinding
import com.example.turborentals.ui.bookingDetails.BookingDetailsActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_booking.*

val selectedRentals = mutableListOf<RentalDetails>()
class BookingActivity : AppCompatActivity() {

    val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType = globalVariables.rentalType
        val factory = BookingViewModelFactory(rentalType,Firebase.auth.currentUser?.uid.toString())
        ViewModelProvider(this,factory).get(BookingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityBookingBinding = DataBindingUtil.setContentView(this,R.layout.activity_booking)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        bookingRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = BookingAdapter()
        bookingRecyclerView.adapter = adapter

        val onSingleClickListener = OnSingleClickListener{
            if(selectedRentals.contains(it))
                selectedRentals.remove(it)
            else
                selectedRentals.add(it)
        }

        adapter.onSingleClickListener = onSingleClickListener

        viewModel.visibility.observe(this,{
            if(it){
                startInfo.visibility = View.VISIBLE
                endInfo.visibility = View.VISIBLE
            }
        })

        viewModel.rentalData.observe(this,{
            adapter.submitList(it)
        })

        val globalVariables =  (application as GlobalVariables)
        viewModel.epochStartDate.observe(this,{
            it?.let {
                globalVariables.startDate = it
            }
        })
        viewModel.epochEndDate.observe(this,{
            it?.let {
                globalVariables.endDate = it
            }
        })

       viewModel.next.observe(this,{

           val intent = Intent(this,BookingDetailsActivity::class.java)
           if(selectedRentals.isNotEmpty()&&globalVariables.startDate!=null&&globalVariables.endDate!=null) {
               if(globalVariables.startDate!! > globalVariables.endDate!!){
                   Toast.makeText(this,"Enter proper End Date",Toast.LENGTH_SHORT).show()
               }else {
                   startActivity(intent)
                   finish()
               }
           }
            else{
                if(selectedRentals.isEmpty())
                Toast.makeText(this,"Select rentals",Toast.LENGTH_SHORT).show()
               else
                   Toast.makeText(this,"Enter Start and End Date",Toast.LENGTH_SHORT).show()
            }

        })

        viewModel.bookingType.observe(this,{
            it?.let {
                if(it){
                    globalVariables.bookingType = "OnRent"
                }
                else{
                    globalVariables.bookingType = "Booked"
                }
            }
        })

    }
    
}