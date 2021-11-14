package com.example.turborentals.ui.rentalsHistory

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.turborentals.GlobalVariables
import com.example.turborentals.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_rentals_history.*

class RentalsHistoryActivity : AppCompatActivity() {

    val viewModel  by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rcNumber = globalVariables.rcNumber
        val rentalType = globalVariables.rentalType
         val factory = RentalsHistoryViewModelFactory(rentalType!!,rcNumber!!,Firebase.auth.currentUser?.uid.toString())
        ViewModelProvider(this,factory).get(RentalsHistoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rentals_history)

        rentalsHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        val rentalAdapter = RentalsHistoryAdapter()
        rentalsHistoryRecyclerView.adapter = rentalAdapter

        viewModel.rentalHistory.observe(this,{
        rentalAdapter.submitList(it)
            Log.v("Main","rental booking history $it")
        })

        val imagesAdapter = RentalsImageAdapter()
        imageRecyclerView.adapter = imagesAdapter

        imageRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true)
        viewModel.url.observe(this,{
         imagesAdapter.submitList(it)
            Log.v("Main","-> images $it")
        })

    }
}