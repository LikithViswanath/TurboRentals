package com.example.turborentals.ui.rentalsHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.data.RentalHistory
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class RentalsHistoryViewModel( private val rentalType : String ,
                               private val rcNumber : String , userId : String ) : ViewModel() {

    private val globalFirestoreRepository = GlobalFirestoreRepository(userId)
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    var rentalHistory = MutableLiveData<List<RentalHistory>>()
    var url = MutableLiveData<List<String>>()

    private var listenerRegistrationRentalHistory = globalFirestoreRepository.fireStoreCollection
        .document(rentalType).collection("Rentals")
        .document(rcNumber).collection("History").addSnapshotListener{ snapshot, _ ->

            if (snapshot != null && snapshot.metadata.hasPendingWrites()){
                rentalHistory.value =  snapshot.toObjects(RentalHistory::class.java)
            }
            if (snapshot != null ) {
                rentalHistory.value =  snapshot.toObjects(RentalHistory::class.java)
            }

        }

    private var listenerImageUrl = globalFirestoreRepository.fireStoreCollection
        .document(rentalType).collection("Rentals")
        .document(rcNumber).addSnapshotListener{ snapshot , _ ->

            if (snapshot != null && snapshot.metadata.hasPendingWrites()){
               val data = snapshot.toObject(RentalDetails::class.java)
                url.value = listOf(
                    data?.imageFront.toString(),
                    data?.imageBack.toString(),
                    data?.imageLeft.toString(),
                    data?.imageRight.toString()
                )
            }
            if (snapshot != null ) {
               val data =  snapshot.toObject(RentalDetails::class.java)
                url.value = listOf(
                    data?.imageFront.toString(),
                    data?.imageBack.toString(),
                    data?.imageLeft.toString(),
                    data?.imageRight.toString()
                )
            }

        }

    init {

        viewModelScope.launch {
            val data = globalFirestoreRepository.fireStoreCollection
                .document(rentalType).collection("Rentals")
                .document(rcNumber).collection("History")
                .get().await().toObjects(RentalHistory::class.java)
            withContext(Dispatchers.Main){
                rentalHistory.value = data
            }

            val imagesUrls = globalFirestoreRepository.fireStoreCollection
                .document(rentalType).collection("Rentals")
                .document(rcNumber).get().await().toObject(RentalDetails::class.java)
            withContext(Dispatchers.Main){
                url.value = listOf( imagesUrls?.imageFront.toString(),
                    imagesUrls?.imageBack.toString(),
                    imagesUrls?.imageLeft.toString(),
                    imagesUrls?.imageRight.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistrationRentalHistory.remove()
        listenerImageUrl.remove()
        viewModelJob.cancel()
    }

}