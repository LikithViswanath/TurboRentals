package com.example.turborentals.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.Rental
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ProfileViewModel( userId : String )  :  ViewModel() {

    private val _rentalData = MutableLiveData<List<Rental>>()
    val rentalData : LiveData<List<Rental>>
    get() = _rentalData
    private val globalFirestoreRepository = GlobalFirestoreRepository(userId)

    private val fireStoreRepo = globalFirestoreRepository.fireStoreCollection
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var listenerRegistration = fireStoreRepo.addSnapshotListener{ snapshot, _ ->

        if (snapshot != null && snapshot.metadata.hasPendingWrites()){
            _rentalData.value =  snapshot.toObjects(Rental::class.java)
        }
        if (snapshot != null ) {
            _rentalData.value =  snapshot.toObjects(Rental::class.java)
        }

    }

    init{
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _rentalData.value = fireStoreRepo.get().await().toObjects(Rental::class.java)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
        viewModelJob.cancel()
    }
}