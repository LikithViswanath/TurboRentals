package com.example.turborentals.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class GlobalFirestoreRepository( private val UserId : String ){
    val fireStoreDataBase = Firebase.firestore
    val fireStoreCollection = fireStoreDataBase.collection(UserId)
    val cloudStorageRef = Firebase.storage.reference
}