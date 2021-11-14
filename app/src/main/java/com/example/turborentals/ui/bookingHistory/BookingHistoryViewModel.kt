package com.example.turborentals.ui.bookingHistory

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turborentals.GlobalVariables
import com.example.turborentals.data.BookingDetails
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.ui.profile.ProfileAdapter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await
import java.util.*

class BookingHistoryViewModel( rentalType : String?,userId : String , application: Application) : ViewModel() {

    val liveRentalDetails = MutableLiveData<List<BookingDetails>>()
    private val globalFirestoreRepository = GlobalFirestoreRepository(userId)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    val globalVariables =  (application as GlobalVariables)
    private var data = mutableListOf<BookingDetails>()

    private var listenerRegistration = globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
        .collection("Bookings").addSnapshotListener{ snapshot, _ ->

            if (snapshot != null && snapshot.metadata.hasPendingWrites()){
                 data = snapshot.toObjects(BookingDetails::class.java)
                 filter()
            }
            if (snapshot != null ) {
                data = snapshot.toObjects(BookingDetails::class.java)
                filter()
            }

        }


    init {
        viewModelScope.launch {
            data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
               .collection("Bookings").get().await().toObjects(BookingDetails::class.java)
            withContext(Main){
                filter()
            }
        }
    }

    private fun filter(){

        when( globalVariables.bookingStatus ){
            "Collect"->{
                collectListener()
            }
            "All"->{
                allListener()
            }
            "Today"->{
                todayListener()
            }
        }

    }

    fun allListener(){
        liveRentalDetails.value = data
    }

    fun runningListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
           data.forEach {
               if(it.bookingStatus=="Running")
                   temp.add(it)
           }
            withContext(Main){
                liveRentalDetails.value = temp
            }
       }

    }

    fun completedListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
            data.forEach {
                if(it.bookingStatus=="Completed")
                    temp.add(it)
            }
            withContext(Main){
                liveRentalDetails.value = temp
            }
        }

    }

    fun upComingListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
            data.forEach {
                if(it.bookingStatus=="Upcoming")
                    temp.add(it)
            }
            withContext(Main){
                liveRentalDetails.value = temp
            }
        }

    }

    fun deliveryListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
            data.forEach {
                if(it.bookingStatus=="Delivery")
                    temp.add(it)
            }
            withContext(Main){
                liveRentalDetails.value = temp
            }
        }

    }

    fun collectListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
            data.forEach {
                if(it.bookingStatus=="Collect")
                    temp.add(it)
            }
            withContext(Main){
                liveRentalDetails.value = temp
            }
        }

    }

    fun todayListener(){

        val temp = mutableListOf<BookingDetails>()
        viewModelScope.launch {
            data.forEach {
                if(DateUtils.isToday(it.startDate!!)||DateUtils.isToday(it.endDate!!))
                temp.add(it)
            }
            withContext(Main){
                liveRentalDetails.value = temp
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
        viewModelJob.cancel()
    }

}