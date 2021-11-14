package com.example.turborentals.ui.editBooking

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.turborentals.DatePair
import com.example.turborentals.alarmDisbatcher.AlarmScheduler
import com.example.turborentals.data.BookingDetails
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.data.Rental
import com.example.turborentals.data.RentalDetails
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.tasks.await

class EditBookingViewModel( var rentalType : String,var phoneNumber : String,userId : String,val application: Application ) : ViewModel() {

    var fullName = MutableLiveData<String?>()
    var phoneOptional = MutableLiveData<String?>()

    var address = MutableLiveData<String?>()

    var totalAmount = MutableLiveData<String?>()
    var securityAmount = MutableLiveData<String?>()
    var paidAmount = MutableLiveData<String?>()
    var balanceAmount = MutableLiveData<String?>()

    var frontIdImageUrl = MutableLiveData<String?>()
    var backIdImageUrl = MutableLiveData<String?>()

    var balanceAmountCollected = MutableLiveData<String?>()
    var fineCollected = MutableLiveData<String?>()
    var securityRefunded = MutableLiveData<String?>()

    var liveRentalDetails = MutableLiveData<List<RentalDetails?>>()
    var bookingStatus = MutableLiveData<String?>()

    var datePair = MutableLiveData<DatePair>()

    var daysOnRent : Int?=null

    private val globalFirestoreRepository = GlobalFirestoreRepository(userId)

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var listenerRegistration = globalFirestoreRepository.fireStoreCollection.document(rentalType)
        .collection("Bookings").document(phoneNumber).addSnapshotListener{ snapshot, _ ->

            if (snapshot != null && snapshot.metadata.hasPendingWrites()){

                val data = snapshot.toObject(BookingDetails::class.java)
                convertDataToObject(data!!)
            }
            if (snapshot != null ) {
                val data = snapshot.toObject(BookingDetails::class.java)
                convertDataToObject(data!!)
            }

        }

    init {

        viewModelScope.launch {
            val data = globalFirestoreRepository.fireStoreCollection.document(rentalType)
                .collection("Bookings").document(phoneNumber).get().await()
                .toObject(BookingDetails::class.java)

            withContext(Main){
                convertDataToObject(data!!)
            }

        }

    }

    private fun convertDataToObject( data : BookingDetails ){

        liveRentalDetails.value = data.rentalsIncluded!!
        frontIdImageUrl.value = data.frontIdImage
        backIdImageUrl.value = data.backIdImage
        fullName.value = data.fullName
        phoneOptional.value = data.phoneOptional.toString()
        address.value = data.address
        totalAmount.value = data.totalAmount.toString()
        securityAmount.value = data.securityAmount.toString()
        paidAmount.value = data.paidAmount.toString()
        balanceAmount.value = data.balanceAmount.toString()

        data.balanceAmountCollected.let {
            balanceAmountCollected.value = it.toString()
        }
        data.fineCollected.let {
            fineCollected.value = it.toString()
        }
        data.securityRefunded.let {
            securityRefunded.value = it.toString()
        }

        bookingStatus.value = data.bookingStatus
        datePair.value  = DatePair(data.startDate,data.endDate)
    }

    fun buttonClickListener( view : View ){

        val updateEarningsData = Data.Builder()
            .putString("rentalType",rentalType)
            .putString("phoneNumber",phoneNumber)
            .putString("totalAmount",totalAmount.value)
            .build()

        val updateHistoryData = Data.Builder()
            .putString("rentalType",rentalType)
            .putString("phoneNumber",phoneNumber)
            .putString("daysOnRent",daysOnRent.toString())
            .putString("startDate",datePair.value?.startDate.toString())
            .putString("EndDate",datePair.value?.endDate.toString())
            .build()

        val setAlarmsData = Data.Builder()
            .putString("alertTime",datePair.value?.endDate.toString())
            .putString("phoneNumber",phoneNumber)
            .putString("rentalType",rentalType)
            .putString("bookingStatus","Running")
            .build()

        val updateRentalsNumbersData = Data.Builder()
            .putString("rentalType",rentalType)
            .putString("phoneNumber",phoneNumber)
            .putString("bookingStatus",bookingStatus.value)
            .build()

        val updateRentalsNumbers = OneTimeWorkRequestBuilder<UpdateRentalsNumbers>().setInputData(updateRentalsNumbersData).build()

        if(bookingStatus.value=="Delivery"){

            val updateRentalStatusData = Data.Builder()
                .putString("rentalType",rentalType)
                .putString("phoneNumber",phoneNumber)
                .putString("updatedStatus","OnRent")
                .build()

            val updateBookingStatusData = Data.Builder()
                .putString("phoneNumber",phoneNumber)
                .putString("bookingStatus","Running")
                .putString("rentalType",rentalType)
                .build()

            val updateBookingStatus = OneTimeWorkRequestBuilder<UpdateBookingStatus>().setInputData(updateBookingStatusData).build()
            val updateRentalStatus = OneTimeWorkRequestBuilder<UpdateRentalStatus>().setInputData(updateRentalStatusData).build()
            val setAlarms = OneTimeWorkRequestBuilder<SetAlarmWorker>().setInputData(setAlarmsData).build()

            WorkManager.getInstance(application)
                .beginWith(listOf(updateBookingStatus,updateRentalStatus,setAlarms,updateRentalsNumbers))
                .enqueue()
        }
        else if(bookingStatus.value=="Collect") {

            val updateBookingStatusData = Data.Builder()
                .putString("phoneNumber",phoneNumber)
                .putString("bookingStatus","Completed")
                .putString("rentalType",rentalType)
                .build()

            val updateRentalStatusData = Data.Builder()
                .putString("rentalType",rentalType)
                .putString("phoneNumber",phoneNumber)
                .putString("updatedStatus","Available")
                .build()

            val updateHistory = OneTimeWorkRequestBuilder<UpdateHistory>().setInputData(updateHistoryData).build()
            val updateBookingStatus = OneTimeWorkRequestBuilder<UpdateBookingStatus>().setInputData(updateBookingStatusData).build()
            val updateRentalStatus = OneTimeWorkRequestBuilder<UpdateRentalStatus>().setInputData(updateRentalStatusData).build()
            val updateEarnings = OneTimeWorkRequestBuilder<UpdateEarnings>().setInputData(updateEarningsData).build()

            WorkManager.getInstance(application)
                .beginWith(listOf(updateHistory,updateBookingStatus,updateEarnings,updateRentalStatus,updateRentalsNumbers))
                .enqueue()
        }

    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration.remove()
        viewModelJob.cancel()
    }

    class UpdateRentalsNumbers(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {

            val rentalType = inputData.getString("rentalType")
            val phoneNumber = inputData.getString("phoneNumber")
            val bookingStatus = inputData.getString("bookingStatus")

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())
            val data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!).collection("Bookings")
                .document(phoneNumber!!).get().await().toObject(BookingDetails::class.java)
            val n = data?.rentalsIncluded?.size

            var onRentRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType).get().await()
                    .toObject(Rental::class.java)?.onRentRentals

            var availableRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType).get().await()
                    .toObject(Rental::class.java)?.availableRentals

            var bookedRentals =
                globalFirestoreRepository.fireStoreCollection.document(rentalType).get().await()
                    .toObject(Rental::class.java)?.bookedRentals

            if(bookingStatus=="Delivery"){

                onRentRentals = onRentRentals?.plus(n!!)
                bookedRentals = bookedRentals?.minus(n!!)

                globalFirestoreRepository.fireStoreDataBase.runBatch { batch->
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "onRentRentals",onRentRentals)
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "bookedRentals",bookedRentals)
                }

            }
            else if(bookingStatus=="Collect"){

                availableRentals = availableRentals?.plus(n!!)
                onRentRentals = onRentRentals?.minus(n!!)

                globalFirestoreRepository.fireStoreDataBase.runBatch { batch->
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "onRentRentals",onRentRentals)
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType),
                        "availableRentals",availableRentals)
                }

            }

            return Result.success()
        }

    }

    class UpdateBookingStatus(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params){
        override suspend fun doWork(): Result {

            val phoneNumber = inputData.getString("phoneNumber")
            val bookingStatus = inputData.getString("bookingStatus")
            val rentalType = inputData.getString("rentalType")

            Firebase.firestore.runBatch { batch->
                batch.update(Firebase.firestore.collection("${Firebase.auth.currentUser?.uid}").document(rentalType!!)
                    .collection("Bookings").document(phoneNumber!!),
                    "bookingStatus",bookingStatus)
            }

            return Result.success()
        }

    }

    class UpdateRentalStatus(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params){
        override suspend fun doWork(): Result {

            val rentalType = inputData.getString("rentalType")
            val phoneNumber = inputData.getString("phoneNumber")
            val updatedStatus = inputData.getString("updatedStatus")

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())
            val data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!).collection("Bookings")
                .document(phoneNumber!!).get().await().toObject(BookingDetails::class.java)
            val rentalsIncluded : MutableList<RentalDetails>? = data?.rentalsIncluded

            for(rental in rentalsIncluded!! ){
                globalFirestoreRepository.fireStoreDataBase.runBatch { batch->
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType)
                        .collection("Rentals").document(rental.RcNumber!!),
                        "status",
                        updatedStatus)
                }
            }

            return Result.success()
        }
    }

    class UpdateEarnings( appContext: Context,params : WorkerParameters ) : CoroutineWorker(appContext,params){
        override suspend fun doWork(): Result {

            val rentalType = inputData.getString("rentalType")
            val phoneNumber = inputData.getString("phoneNumber")
            val totalAmount = inputData.getString("totalAmount")?.toLong()

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())
            val data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!).collection("Bookings")
                .document(phoneNumber!!).get().await().toObject(BookingDetails::class.java)
            val rentalsIncluded : MutableList<RentalDetails>? = data?.rentalsIncluded

            val n = rentalsIncluded?.size

            for(rental in rentalsIncluded!! ){

                val previousEarnings = rental.earnings

                globalFirestoreRepository.fireStoreDataBase.runBatch { batch->
                    batch.update(globalFirestoreRepository.fireStoreCollection.document(rentalType)
                        .collection("Rentals").document(rental.RcNumber!!),
                        "earnings",
                        (totalAmount!!/n!!)+previousEarnings!!)
                }
            }

            return Result.success()
        }
    }

    class UpdateHistory(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
        params){
        override suspend fun doWork(): Result {

            val rentalType = inputData.getString("rentalType")
            val phoneNumber = inputData.getString("phoneNumber")
            val daysOnRent = inputData.getString("daysOnRent")?.toInt()
            val startDate = inputData.getString("startDate")
            val endDate = inputData.getString("EndDate")?.toLong()

            val globalFirestoreRepository = GlobalFirestoreRepository(Firebase.auth.currentUser?.uid.toString())
            val data = globalFirestoreRepository.fireStoreCollection.document(rentalType!!).collection("Bookings")
                .document(phoneNumber!!).get().await().toObject(BookingDetails::class.java)
            val rentalsIncluded : MutableList<RentalDetails>? = data?.rentalsIncluded

            for( rental in rentalsIncluded!! ){

                val rentalHistory = mapOf(
                    "RcNumber" to rental.RcNumber,
                    "modelName" to rental.modelName,
                    "daysOnRent" to daysOnRent,
                    "currentKm" to 0,
                    "updatedKm" to 0,
                    "endDate" to endDate
                )
                globalFirestoreRepository.fireStoreCollection
                    .document(rentalType).collection("Rentals")
                    .document(rental.RcNumber!!).collection("History")
                    .document(startDate!!).set(rentalHistory)
            }

            return Result.success()
        }

    }

    class SetAlarmWorker( appContext: Context,params : WorkerParameters ) : Worker(appContext,params){
        override fun doWork(): Result {

            val alertTime = inputData.getString("alertTime")?.toLong()
            val phoneNumber = inputData.getString("phoneNumber")
            val rentalType = inputData.getString("rentalType")
            val bookingStatus = inputData.getString("bookingStatus")

            val alarmManager = applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val alarmScheduler = AlarmScheduler(applicationContext,alarmManager)
            alarmScheduler.scheduleAnAlarm(alertTime!!,phoneNumber!!,rentalType!!,bookingStatus!!)

            return Result.success()
        }
    }

}

