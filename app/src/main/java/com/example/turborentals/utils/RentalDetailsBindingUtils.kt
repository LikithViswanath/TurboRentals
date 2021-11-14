package com.example.turborentals.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turborentals.R
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.ui.bookingHistory.BookingHistoryChildAdapter
import java.util.*


@BindingAdapter("setStatusTextAndColor")
fun TextView.setStatusTextAndColor( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text  = it.status
        when( it.status ){
            "Available"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_yellow))
            }
            "OnRent"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_green))
            }
            "Booked"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_red))
            }
        }
    }
}

@BindingAdapter("setRcNumberModelNumberText")
fun TextView.setRcNumberModelNumberText( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        val str = "${it.RcNumber}    ${it.modelName}"
        text = str
    }
}

/*
--------------------------------------------------------------------------------------------------
*/

@BindingAdapter("setImage")
fun setImage(view: ImageView, url : String? ){
    url?.let {
        Glide.with(view.context).load(url).into(view)
    }
}

/*
--------------------------------------------------------------------------------------------------
*/

@BindingAdapter( value = ["setRentalDetails"] )
fun RecyclerView.setRentalDetails(selectedRentals : List<RentalDetails>? ){
    selectedRentals.let {
        val bookingHistoryChildAdapter = BookingHistoryChildAdapter()
        bookingHistoryChildAdapter.submitList(selectedRentals)
        adapter = bookingHistoryChildAdapter
    }
}

/*
--------------------------------------------------------------------------------------------------
*/

@BindingAdapter("setModelName")
fun TextView.setModelName( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = it.modelName
    }
}

@BindingAdapter("setRcNumber")
fun TextView.setRcNumber( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = it.RcNumber
    }
}

@BindingAdapter("setStatus")
fun TextView.setStatus( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = it.status
        when( it.status ){
            "Available"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_yellow))
            }
            "OnRent"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_green))
            }
            "Booked"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_red))
            }
        }
    }
}

@BindingAdapter("setEarnings")
fun TextView.setEarnings( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        val earnings = "Rs. ${it.earnings}"
        text = earnings
    }
}

@BindingAdapter("setOnRentDays")
fun TextView.setOnRentDays( rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        val days = "${it.daysOnRent} Days"
        text = days
    }
}


/*
--------------------------------------------------------------------------------------------------
*/

@BindingAdapter("setKm00")
fun TextView.setKm00(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm01")
fun TextView.setKm01(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[1].toString()
        else if( it.currentKm.toString().length==4)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm02")
fun TextView.setKm02(rentalDetails: RentalDetails? ){
    rentalDetails?.let {

        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[2].toString()
        else if( it.currentKm.toString().length==4)
            it.currentKm.toString()[1].toString()
        else if(it.currentKm.toString().length==3)
            it.currentKm.toString()[0].toString()
        else
            "0"

    }
}

@BindingAdapter("setKm03")
fun TextView.setKm03(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[3].toString()
        else if( it.currentKm.toString().length==4)
            it.currentKm.toString()[2].toString()
        else if(it.currentKm.toString().length==3)
            it.currentKm.toString()[1].toString()
        else if(it.currentKm.toString().length==2)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm04")
fun TextView.setKm04(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[4].toString()
        else if( it.currentKm.toString().length==4)
            it.currentKm.toString()[3].toString()
        else if(it.currentKm.toString().length==3)
            it.currentKm.toString()[2].toString()
        else if(it.currentKm.toString().length==2)
            it.currentKm.toString()[1].toString()
        else if(it.currentKm.toString().length==1)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

/*-----------------------------------------------------*/

@BindingAdapter("setKm10")
fun TextView.setKm10(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm11")
fun TextView.setKm11(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[1].toString()
        else if( it.updatedKm.toString().length==4)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm12")
fun TextView.setKm12(rentalDetails: RentalDetails? ){
    rentalDetails?.let {

        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[2].toString()
        else if( it.updatedKm.toString().length==4)
            it.updatedKm.toString()[1].toString()
        else if(it.updatedKm.toString().length==3)
            it.updatedKm.toString()[0].toString()
        else
            "0"

    }
}

@BindingAdapter("setKm13")
fun TextView.setKm13(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[3].toString()
        else if( it.updatedKm.toString().length==4)
            it.updatedKm.toString()[2].toString()
        else if(it.updatedKm.toString().length==3)
            it.updatedKm.toString()[1].toString()
        else if(it.updatedKm.toString().length==2)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm14")
fun TextView.setKm14(rentalDetails: RentalDetails? ){
    rentalDetails?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[4].toString()
        else if( it.updatedKm.toString().length==4)
            it.updatedKm.toString()[3].toString()
        else if(it.updatedKm.toString().length==3)
            it.updatedKm.toString()[2].toString()
        else if(it.updatedKm.toString().length==2)
            it.updatedKm.toString()[1].toString()
        else if(it.updatedKm.toString().length==1)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}