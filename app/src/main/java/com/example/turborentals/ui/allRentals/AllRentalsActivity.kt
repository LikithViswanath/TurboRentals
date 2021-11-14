package com.example.turborentals.ui.allRentals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.example.turborentals.databinding.ActivityAllRentalsBinding
import com.example.turborentals.ui.rentalsHistory.RentalsHistoryActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_all_rentals.*

class AllRentalsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType =  globalVariables.rentalType
        val factory = AllRentalsViewModelFactory(rentalType!!,Firebase.auth.currentUser?.uid.toString(),application)
        ViewModelProvider(this,factory).get(AllRentalsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAllRentalsBinding = DataBindingUtil.setContentView(this,R.layout.activity_all_rentals)
        binding.viewModel = viewModel

        binding.allRentalRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AllRentalsAdapter()
        allRentalRecyclerView.adapter = adapter

        viewModel.allRentalDetails.observe(this,{
            adapter.submitList(it)
        })

        adapter.setHistoryListener = SetHistoryListener {
            val intent  = Intent(this,RentalsHistoryActivity::class.java)
            val globalVariables =  (application as GlobalVariables)
             globalVariables.rcNumber = it.RcNumber
            startActivity(intent)
        }
    }
}