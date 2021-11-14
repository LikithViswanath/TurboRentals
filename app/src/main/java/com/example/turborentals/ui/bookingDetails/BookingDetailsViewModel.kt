package com.example.turborentals.ui.bookingDetails

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.turborentals.DatePair
import com.example.turborentals.PhotoActivity
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.Rental
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.ui.bookings.selectedRentals
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.math.max

class BookingDetailsViewModel( val rentalType : String ,userId : String?,val application: Application ) : ViewModel() {

    var fullName: String? = null
    var phoneNumber: String? = null
    var phoneOptional: String? = null

    var address: String? = null

    var totalAmount: String? = null
    var securityAmount: String? = null
    var paidAmount: String? = null
    var balanceAmount: String? = null

    var startDate: String? = null
    var endDate: String? = null

    var frontIdImageUri = MutableLiveData<Uri?>()
    var backIdImageUri = MutableLiveData<Uri?>()

    var epochStartDate: Long? = null
    var epochEndDate: Long? = null

    var scheduleAnAlarm = MutableLiveData<DatePair>()


    companion object {
        var rentalsIncluded = mutableListOf<RentalDetails>()
    }

    init {
        rentalsIncluded = selectedRentals
    }

    var bookingType: String? = null

    fun onFrontIdImageClicked(phoneActivity: PhotoActivity) {
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }

    fun onBackIdImageClicked(phoneActivity: PhotoActivity) {
        phoneActivity.phoneActivityResultLauncher.launch(phoneActivity.photoActivityResultContract)
    }

    fun onBookNowClicked(view: View) {

        val imageFront = frontIdImageUri.value
        val imageBack = backIdImageUri.value

        if (
            fullName.isNullOrEmpty()
            && phoneNumber.isNullOrEmpty()
            && phoneOptional.isNullOrEmpty()
            && address.isNullOrEmpty()
            && totalAmount.isNullOrEmpty()
            && securityAmount.isNullOrEmpty()
            && paidAmount.isNullOrEmpty()
            && balanceAmount.isNullOrEmpty()
            && startDate.isNullOrEmpty()
            && endDate.isNullOrEmpty()
            && epochStartDate == null
            && epochEndDate == null
            && imageFront == null
            && imageBack == null
        ) {
            Toast.makeText(view.context, "Fill All the Fields", Toast.LENGTH_SHORT).show()
        } else {

            scheduleAnAlarm.value = DatePair(epochStartDate,epochEndDate)

            val bookingInfoData = Data.Builder()
                .putString("startDate", epochStartDate.toString())
                .putString("endDate", epochEndDate.toString())
                .putString("fullName", fullName)
                .putString("phoneOptional", phoneOptional)
                .putString("address", address)
                .putString("balanceAmount", balanceAmount)
                .putString("totalAmount", totalAmount)
                .putString("securityAmount", securityAmount)
                .putString("paidAmount", paidAmount)
                .putString("rentalType", rentalType)
                .putString("phoneNumber", phoneNumber)
                .putString("bookingType", bookingType)
                .build()

            val frontPhotoIdData = Data.Builder()
                .putString("phoneNumber", phoneNumber)
                .putString("imageDesc", "frontIdImage")
                .putString("imageUri", frontIdImageUri.value.toString())
                .putString("rentalType", rentalType)
                .build()

            val backPhotoIdData = Data.Builder()
                .putString("phoneNumber", phoneNumber)
                .putString("imageDesc", "backIdImage")
                .putString("imageUri", backIdImageUri.value.toString())
                .putString("rentalType", rentalType)
                .build()

            val statusUpdateData = Data.Builder()
                .putString("startDate", epochStartDate.toString())
                .putString("endDate", epochEndDate.toString())
                .putString("rentalType", rentalType)
                .putString("bookingType", bookingType)
                .build()

            val rentalUpdateNumberData = Data.Builder()
                .putString("rentalType", rentalType)
                .putString("bookingType", bookingType)
                .build()

            val bookingInfoDataWorker = OneTimeWorkRequestBuilder<AddBookingInfoToDataBase>().setInputData(bookingInfoData).build()
            val frontImageIdWorker = OneTimeWorkRequestBuilder<AddPhotoId>().setInputData(frontPhotoIdData).build()
            val backImageIdWorker = OneTimeWorkRequestBuilder<AddPhotoId>().setInputData(backPhotoIdData).build()
            val statusUpdateWorker = OneTimeWorkRequestBuilder<UpdateStatus>().setInputData(statusUpdateData).build()
            val rentalUpdateNumbersWorker = OneTimeWorkRequestBuilder<UpdateRentalsNumbers>().setInputData(rentalUpdateNumberData).build()

            WorkManager.getInstance(application)
                .beginWith(bookingInfoDataWorker)
                .then(listOf(frontImageIdWorker,backImageIdWorker,statusUpdateWorker,rentalUpdateNumbersWorker))
                .enqueue()

        }
    }

    class AddBookingInfoToDataBase(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())

            val epochStartDate =inputData.getString("startDate")
            val epochEndDate = inputData.getString("endDate")
            val fullName = inputData.getString("fullName")
            val phoneOptional = inputData.getString("phoneOptional")
            val address = inputData.getString("address")
            val balanceAmount =inputData.getString("balanceAmount")
            val totalAmount = inputData.getString("totalAmount")
            val securityAmount = inputData.getString("securityAmount")
            val paidAmount = inputData.getString("paidAmount")
            val rentalType = inputData.getString("rentalType")
            val phoneNumber = inputData.getString("phoneNumber")
            val bookingType = inputData.getString("bookingType")

            val data = hashMapOf(
                "frontIdImage" to null,
                "backIdImage" to null,
                "balanceAmountCollected" to 0,
                "securityRefunded" to 0,
                "fineCollected" to 0,
                "startDate" to epochStartDate?.toLong(),
                "endDate" to epochEndDate?.toLong(),
                "fullName" to fullName,
                "phoneOptional" to phoneOptional?.toLong(),
                "address" to address,
                "balanceAmount" to balanceAmount?.toLong(),
                "totalAmount" to totalAmount?.toLong(),
                "securityAmount" to securityAmount?.toLong(),
                "paidAmount" to paidAmount?.toLong(),
                "rentalsIncluded" to rentalsIncluded,
                "bookingStatus" to ""
            )

            if(bookingType=="OnRent")
                data["bookingStatus"] = "Running"
            else
                data["bookingStatus"] = "Upcoming"

            globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
                .collection("Bookings")
                .document(phoneNumber!!).set(data).await()
            return Result.success()
        }

    }

    class AddPhotoId(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {

            val phoneNumber = inputData.getString("phoneNumber")
            val imageDesc = inputData.getString("imageDesc")
            val userId = Firebase.auth.currentUser?.uid.toString()
            val imageUri = inputData.getString("imageUri")?.toUri()
            val rentalType = inputData.getString("rentalType")

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())
            var imageUrl: String? = null

            globalFirestoreRepository.cloudStorageRef.child("$userId/$rentalType").child("Bookings")
                .child("$phoneNumber")
                .child(imageDesc!!)
                .putFile(imageUri!!).await()

            globalFirestoreRepository.cloudStorageRef.child("$userId/$rentalType").child("Bookings")
                .child("$phoneNumber")
                .child(imageDesc).downloadUrl.addOnSuccessListener {
                    imageUrl = it.toString()
                }.await()

            globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->
                batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
                    .collection("Bookings").document("$phoneNumber"),
                    imageDesc,
                    imageUrl)
            }

            return Result.success()
        }

    }

    class UpdateStatus(context: Context, workerParams: WorkerParameters) :
        CoroutineWorker(context, workerParams) {
        override suspend fun doWork(): Result {

            val epochStartDate =inputData.getString("startDate")?.toLong()
            val epochEndDate = inputData.getString("endDate")?.toLong()
            val rentalType = inputData.getString("rentalType")
            val bookingType = inputData.getString("bookingType")

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())

            for (rentals in rentalsIncluded) {

                globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->

                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType!!)
                        .collection("Rentals").document(rentals.RcNumber!!),
                        "status",
                        bookingType)

                    val days = (((epochEndDate!!) - (epochStartDate!!)) / (24 * 60 * 60 * 1000))
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType)
                        .collection("Rentals").document(rentals.RcNumber!!),
                        "daysOnRent",
                        days)
                }
            }

            return Result.success()
        }

    }

    class UpdateRentalsNumbers(appContext: Context,
                               params: WorkerParameters
    ) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {

            Log.v("Coroutine","Updating Numbers")

            val rentalType = inputData.getString("rentalType")
            val bookingType = inputData.getString("bookingType")

            val globalFirestoreRepository =
                GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())

            val onRentRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType!!).get().await()
                    .toObject(Rental::class.java)?.onRentRentals

            var availableRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType).get().await()
                    .toObject(Rental::class.java)?.availableRentals

            val bookedRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType).get().await()
                    .toObject(Rental::class.java)?.bookedRentals

            availableRentals = max(0, availableRentals!! - rentalsIncluded.size)

            globalFirestoreRepository.fireStoreDataBase.runBatch { batch ->

                if (bookingType == "OnRent")
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "onRentRentals",
                        onRentRentals?.plus(rentalsIncluded.size)
                    )
                else
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "bookedRentals",
                        bookedRentals?.plus(rentalsIncluded.size)
                    )

                batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                    "availableRentals",
                    availableRentals
                )

            }
            return Result.success()
        }
    }
}


