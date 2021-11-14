package com.example.turborentals.ui.rentalsHistory

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.turborentals.data.RentalHistory
import java.util.*

@BindingAdapter("setModelName")
fun TextView.setModelName( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = it.modelName
    }
}

@BindingAdapter("setRcNumber")
fun TextView.setRcNumber( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = it.RcNumber
    }
}

@BindingAdapter("setDays")
fun TextView.setDays( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        val difference: Long = (it.endDate!!).minus(it.startDate?.toLong()!!)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val dayDifference = "$differenceDates days"
        text = dayDifference
    }
}

fun getShortDate(ts:Long?):String{
    if(ts == null) return ""
    val calendar = Calendar.getInstance(Locale.getDefault())
    calendar.timeInMillis = ts
    return android.text.format.DateFormat.format("dd MMM yyyy", calendar).toString()
}

@BindingAdapter("setDates")
fun TextView.setDates( rentalHistory: RentalHistory? ){
    rentalHistory?.let {

        val start = getShortDate(it.startDate?.toLong())
        val end = getShortDate(it.endDate)
        "$start - $end".also {  str->
            text = str
        }
    }
}

@BindingAdapter("setKm00")
fun TextView.setKm00(rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm01")
fun TextView.setKm01( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = if(it.currentKm.toString().length==5)
            it.currentKm.toString()[1].toString()
        else if( it.currentKm.toString().length==4)
            it.currentKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm02")
fun TextView.setKm02( rentalHistory: RentalHistory? ){
    rentalHistory?.let {

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
fun TextView.setKm03( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
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
fun TextView.setKm04( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
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
fun TextView.setKm10( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm11")
fun TextView.setKm11( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
        text = if(it.updatedKm.toString().length==5)
            it.updatedKm.toString()[1].toString()
        else if( it.updatedKm.toString().length==4)
            it.updatedKm.toString()[0].toString()
        else
            "0"
    }
}

@BindingAdapter("setKm12")
fun TextView.setKm12( rentalHistory: RentalHistory? ){
    rentalHistory?.let {

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
fun TextView.setKm13( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
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
fun TextView.setKm14( rentalHistory: RentalHistory? ){
    rentalHistory?.let {
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


