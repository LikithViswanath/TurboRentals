package com.example.turborentals.ui.bookings

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.RentalDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class BookingViewModel(private val rentalType : String?,userId : String) : ViewModel() {

    var rentalData = MutableLiveData<List<RentalDetails>>()

    var startDate = MutableLiveData<String?>()
    var endDate = MutableLiveData<String?>()

    var visibility = MutableLiveData<Boolean>()
    var next = MutableLiveData<Boolean>()

    var epochStartDate = MutableLiveData<Long?>()
    var epochEndDate = MutableLiveData<Long?>()

    var bookingType = MutableLiveData<Boolean?>()

    private val globalFirestoreRepository = GlobalFirestoreRepository(userId)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var listenerRegistration = globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
        .collection("Rentals").whereEqualTo("status","Available").addSnapshotListener{ snapshot , _ ->

            if (snapshot != null && snapshot.metadata.hasPendingWrites()){
                  rentalData.value = snapshot.toObjects(RentalDetails::class.java)
            }
            if (snapshot != null ) {
                  rentalData.value = snapshot.toObjects(RentalDetails::class.java)
            }
        }

    init {
        startDate.value = "Today/Future"
        endDate.value = "Select dd/mm/yy"
        viewModelScope.launch {
           val data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
               .collection("Rentals").whereEqualTo("status","Available").get().await().toObjects(RentalDetails::class.java)
          withContext(Main){
              rentalData.value = data
          }
        }
    }

    fun bookForTodayListener( view: View ){
        visibility.value = true
        epochStartDate.value = System.currentTimeMillis()
       startDate.value = getDate(System.currentTimeMillis())
        bookingType.value = true
    }

    fun nextButtonListener( view: View ){
        next.value = true
    }

    fun bookForFutureListener( view: View ){

        bookingType.value = false

        val now = Calendar.getInstance()
        var epoch : Long = 0
        val datePicker = DatePickerDialog(
            view.context, { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(
                    view.context, { _, hourOfDay, minute ->
                        epoch = SimpleDateFormat("MM/dd/yy HH:mm").parse((month + 1).toString()
                                + "/" + dayOfMonth.toString() + "/"
                                + year.toString() + " " + hourOfDay.toString()
                                + ":" + minute.toString()).time
                        startDate.value = getDate(epoch)
                        visibility.value = true
                        epochStartDate.value = epoch
                    },
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
                )
                timePicker.show()
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    fun endDateListener( view: View ){
        val now = Calendar.getInstance()
        var epoch : Long = 0
        val datePicker = DatePickerDialog(
            view.context, { _, year, month, dayOfMonth ->
                val timePicker = TimePickerDialog(
                    view.context, { _, hourOfDay, minute ->
                        epoch = SimpleDateFormat("MM/dd/yy HH:mm").parse((month + 1).toString()
                                + "/" + dayOfMonth.toString() + "/"
                                + year.toString() + " " + hourOfDay.toString()
                                + ":" + minute.toString()).time
                        endDate.value = getDate(epoch)
                        visibility.value = true
                        epochEndDate.value = epoch
                    },
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
                )
                timePicker.show()
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        listenerRegistration.remove()
    }

    private fun getDate(ts:Long?):String{
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = ts!!
        return android.text.format.DateFormat.format("E, dd MMM yyyy HH:mm", calendar).toString()
    }

}