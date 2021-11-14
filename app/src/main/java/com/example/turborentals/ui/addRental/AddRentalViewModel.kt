package com.example.turborentals.ui.addRental

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.turborentals.PhotoActivity
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.Rental
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*

class AddRentalViewModel( private var rentalType: String?,
                          private val application : Application,
                          val userId: String): ViewModel(){

    var rcNumber : String?=null
    var modelName : String?=null
    var currentKm : String?=null

    var label : String? = null
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    var imageUriLeft = MutableLiveData<Uri?>()
    var imageUriRight = MutableLiveData<Uri?>()
    var imageUriFront = MutableLiveData<Uri?>()
    var imageUriBack = MutableLiveData<Uri?>()
    var error = MutableLiveData<String>()

    init {
        label = rentalType
    }

    fun onAddButtonClicked( view: View ){

        val imageFront = imageUriFront.value.toString()
        val imageBack = imageUriBack.value.toString()
        val imageLeft = imageUriLeft.value.toString()
        val  imageRight = imageUriRight.value.toString()

        if(rcNumber.isNullOrEmpty()&&modelName.isNullOrEmpty()&&currentKm.isNullOrEmpty()
            && imageFront.isEmpty()&& imageBack.isEmpty() &&imageLeft.isEmpty()&& imageRight.isEmpty()
        ) {
            error.value = "Enter All The Fields...!"
        }
        else{

            label="Add New $rentalType"

            val imageUriLeftString = imageUriLeft.value.toString()
            val imageUriRightString = imageUriRight.value.toString()
            val imageUriFrontString = imageUriFront.value.toString()
            val imageUriBackString = imageUriBack.value.toString()

            val rentalData = Data.Builder()
                .putString("rentalType",rentalType)
                .putString("rcNumber",rcNumber)
                .putString("modelName",modelName)
                .putString("currentKm",currentKm)
                .build()

            val frontImageData = Data.Builder()
                .putString("rcNumber",rcNumber)
                .putString("imageDesc","imageFront")
                .putString("imageUri",imageUriFrontString)
                .putString("rentalType",rentalType)
                .build()

            val backImageData = Data.Builder()
                .putString("rcNumber",rcNumber)
                .putString("imageDesc","imageBack")
                .putString("imageUri",imageUriBackString)
                .putString("rentalType",rentalType)
                .build()

            val leftImageData = Data.Builder()
                .putString("rcNumber",rcNumber)
                .putString("imageDesc","imageLeft")
                .putString("imageUri",imageUriLeftString)
                .putString("rentalType",rentalType)
                .build()

            val rightImageData = Data.Builder()
                .putString("rcNumber",rcNumber)
                .putString("imageDesc","imageRight")
                .putString("imageUri",imageUriRightString)
                .putString("rentalType",rentalType)
                .build()

            val rentalDataWorker = OneTimeWorkRequestBuilder<AddRentalToDataBase>().setInputData(rentalData).build()
            val frontImageWorker = OneTimeWorkRequestBuilder<AddImagesToRental>().setInputData(frontImageData).build()
            val backImageWorker = OneTimeWorkRequestBuilder<AddImagesToRental>().setInputData(backImageData).build()
            val leftImageWorker = OneTimeWorkRequestBuilder<AddImagesToRental>().setInputData(leftImageData).build()
            val rightImageWorker = OneTimeWorkRequestBuilder<AddImagesToRental>().setInputData(rightImageData).build()


            WorkManager.getInstance(application)
                .beginWith(rentalDataWorker)
                .then(listOf(frontImageWorker,backImageWorker,leftImageWorker,rightImageWorker))
                .enqueue()

        }
    }

    fun onLeftImageClicked( phoneActivity : PhotoActivity){
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }

    fun onRightImageClicked( phoneActivity : PhotoActivity){
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }

    fun onFrontImageClicked( phoneActivity : PhotoActivity){
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }

    fun onBackImageClicked( phoneActivity : PhotoActivity){
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }



    class AddRentalToDataBase(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
       override suspend fun doWork(): Result {

               val status = "Available"
               val rcNumber = inputData.getString("rcNumber")
               val modelName = inputData.getString("modelName")
               val currentKm = inputData.getString("currentKm")
               val rentalType = inputData.getString("rentalType")
               val userId = Firebase.auth.currentUser?.uid.toString()

               val data = hashMapOf(
                   "RcNumber" to rcNumber,
                   "modelName" to modelName,
                   "currentKm" to currentKm?.toLong(),
                   "status" to status,
                   "imageLeft" to null,
                   "imageRight" to null,
                   "imageFront" to null,
                   "imageBack" to null,
                   "earnings" to 0,
                   "daysOnRent" to 0,
                   "updatedKm" to 0
               )

           val globalFirestoreRepository = GlobalFirestoreRepository(userId)
           val fireStoreCollection = globalFirestoreRepository.fireStoreCollection

           val availableRentals =
               fireStoreCollection.document(rentalType!!).get().await()
                   .toObject(Rental::class.java)?.availableRentals

           val totalRentals =
               fireStoreCollection.document(rentalType).get().await()
                   .toObject(Rental::class.java)?.totalRentals

           globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->

               batch.update(fireStoreCollection.document(rentalType),
                   "totalRentals",
                   totalRentals?.plus(1)
               )

               batch.update(fireStoreCollection.document(rentalType),
                   "availableRentals",
                   availableRentals?.plus(1)
               )

           }

               globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->
                   batch.set(globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
                       .collection("Rentals").document(rcNumber!!), data)
               }.await()

           return Result.success()
       }
   }

    class AddImagesToRental(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {

            val rcNumber = inputData.getString("rcNumber")
            val imageUri = inputData.getString("imageUri")?.toUri()
            val userId = Firebase.auth.currentUser?.uid.toString()
            val imageDesc = inputData.getString("imageDesc")
            val rentalType = inputData.getString("rentalType")

            uploadImage(rcNumber!!, imageDesc!!, imageUri!!, userId, rentalType)
            return Result.success()
        }

        private suspend fun uploadImage(rcNumber : String, imageDesc : String, imageUri : Uri, userId : String, rentalType: String?){

            val globalFirestoreRepository = GlobalFirestoreRepository(userId)
            try {
                globalFirestoreRepository.cloudStorageRef
                    .child("$userId/$rentalType").child("Rentals").child(rcNumber)
                    .child(imageDesc).putFile(imageUri).await()
            } catch (e: Exception) {
                Log.v("Main", "$e.message->upload")
            }

            try {

                var imageUrl: String? = null
                globalFirestoreRepository.cloudStorageRef
                    .child("$userId/$rentalType").child("Rentals").child(rcNumber)
                    .child(imageDesc).downloadUrl.addOnSuccessListener {
                        imageUrl = it.toString()
                        Log.v("Main","$it")
                    }.await()

                globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(
                        rentalType!!)
                        .collection("Rentals").document(rcNumber),
                        imageDesc,
                        imageUrl.toString())
                }

            } catch (e: Exception) {
                Log.v("Main", "$e.message->download")
            }

        }

    }

}

