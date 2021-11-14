package com.example.turborentals.utils

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.turborentals.R
import com.example.turborentals.data.BookingDetails
import java.util.*
import kotlin.math.min


@BindingAdapter("setDates")
fun TextView.setDates( bookingDetails: BookingDetails? ){
    bookingDetails?.let {
        val start = getShortDate(it.startDate)
        val end = getShortDate(it.endDate)
        val dates = "$start - $end"
        text = dates
    }
}

@BindingAdapter("setDays")
fun TextView.setDays( bookingDetails: BookingDetails? ){
    bookingDetails?.let {
        val difference: Long = it.endDate!! - it.startDate!!
        val differenceDates : Long = difference / (24 * 60 * 60 * 1000)
        val dayDifference = "$differenceDates days"
        text = dayDifference
    }
}

@BindingAdapter("setBookingStatus")
fun TextView.setBookingStatus( bookingDetails: BookingDetails? ){
    bookingDetails?.let {
        text = it.bookingStatus
        when(it.bookingStatus){
            "Completed"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_green))
            }
            "Upcoming"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_blue))
            }
            "Running"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_yellow))
            }
            "Delivery"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_red))
            }
            "Collect"->{
                background.setTint(ContextCompat.getColor(context, R.color.good_pink))
            }
        }
    }
}

@BindingAdapter("setEarnings")
fun TextView.setEarnings( bookingDetails: BookingDetails? ){
    bookingDetails?.let {
        val earnings = "Rs. ${it.totalAmount}"
        text = earnings
    }
}

fun getShortDate(ts:Long?):String{
    if(ts == null) return ""
    val calendar = Calendar.getInstance(Locale.getDefault())
    calendar.timeInMillis = ts
    return android.text.format.DateFormat.format("dd MMM yyyy", calendar).toString()
}