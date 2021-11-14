package com.example.turborentals.ui.allRentals

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turborentals.GlobalVariables
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.RentalDetails
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await

class AllRentalsViewModel( private val rentalType: String?,private val userId : String?,val application: Application ):ViewModel() {

    var rentalsName : String ? =null
    init {
        rentalsName = rentalType
    }
    var allRentalDetails = MutableLiveData<MutableList<RentalDetails>>()

    private val globalVariables =  (application as GlobalVariables)
    private val globalFirestoreRepository = GlobalFirestoreRepository(userId!!)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var data : MutableList<RentalDetails>? = null

    private var listenerRegistration = globalFirestoreRepository.fireStoreCollection
        .document(rentalType!!).collection("Rentals").addSnapshotListener{ snapshot, _ ->

        if (snapshot != null && snapshot.metadata.hasPendingWrites()){
            data =  snapshot.toObjects(RentalDetails::class.java)
            filter()
        }
        if (snapshot != null ) {
            data = snapshot.toObjects(RentalDetails::class.java)
            filter()
        }

    }

    init {

        viewModelScope.launch {
           data = globalFirestoreRepository.fireStoreCollection
               .document(rentalType!!).collection("Rentals")
               .get().await().toObjects(RentalDetails::class.java)

            withContext(Main) {
                filter()
            }
        }
    }

    private fun filter(){

        when( globalVariables.rentalStatus ) {
            "All"-> {
                allRentalDetails.value = data!!
            }
            "Booked"->{
                bookedRentals()
            }
            "OnRent"->{
                onRentRentals()
            }
            "Available"->{
                availableRentals()
            }
        }

    }

    fun allRentals(){
        allRentalDetails.value = data!!
    }

    fun bookedRentals(){
        viewModelScope.launch {
            val temp = mutableListOf<RentalDetails>()
            data?.forEach {
                if (it.status == "Booked") {
                    temp.add(it)
                }
            }
            withContext(Main){
                allRentalDetails.value = temp
            }
        }
    }

    fun onRentRentals(){
        viewModelScope.launch {
            val temp = mutableListOf<RentalDetails>()
            data?.forEach {
                if (it.status == "OnRent") {
                    temp.add(it)
                }
            }
            withContext(Main){
                allRentalDetails.value = temp
            }
        }
    }

    fun availableRentals(){
        viewModelScope.launch {
            val temp = mutableListOf<RentalDetails>()
            data?.forEach {
                if (it.status == "Available") {
                    temp.add(it)
                }
            }
            withContext(Main){
                allRentalDetails.value = temp
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
        viewModelJob.cancel()
    }

}