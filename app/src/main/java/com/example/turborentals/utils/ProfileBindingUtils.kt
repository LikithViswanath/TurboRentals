package com.example.turborentals.ui.profile

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.turborentals.data.Rental

@BindingAdapter("setRentalText")
fun TextView.setRentalText( item : Rental ){
    text= item.rental
}

@BindingAdapter("setTotalRentalsText")
fun TextView.setTotalRentalsText( item: Rental ){
    text = item.totalRentals.toString()
}

@BindingAdapter("setAvailableRentalsText")
fun TextView.setAvailableRentalsText( item: Rental ){
    text = item.availableRentals.toString()
}

@BindingAdapter("setOnRentText")
fun TextView.setOnRentText( item: Rental ){
    text = item.onRentRentals.toString()
}

@BindingAdapter("setBookedRentalsText")
fun TextView.setBookedRentalsText(item: Rental){
    text = item.bookedRentals.toString()
}