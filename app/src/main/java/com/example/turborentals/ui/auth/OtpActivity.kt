package com.example.turborentals.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.turborentals.R
import com.example.turborentals.data.GlobalFirestoreRepository
import com.example.turborentals.ui.profile.ProfileActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity() {

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OtpActivity,"Unsuccessful..!",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = token
            }
        }

        val intent = intent
        val phoneNumber = intent.getStringExtra("phoneNumber")
        startPhoneNumberVerification(phoneNumber!!)

        b2.setOnClickListener {
            verifyPhoneNumberWithCode(storedVerificationId,t2.text.toString())
        }

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"SuccessFull..!!",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ProfileActivity::class.java)

                    val data = hashMapOf(
                        "rental" to "Car",
                        "totalRentals" to 0,
                        "availableRentals" to 0,
                        "onRentRentals" to 0,
                        "bookedRentals" to 0
                    )

                    verifyProgressBar.visibility = View.VISIBLE
                    verifyProgressTextView.visibility = View.VISIBLE

                    val fireStone = GlobalFirestoreRepository(auth.currentUser?.uid.toString())
                    CoroutineScope(Dispatchers.IO).launch{
                        fireStone.fireStoreCollection.document("carRenal").set(data).await()
                        data["rental"] = "Bike"
                        fireStone.fireStoreCollection.document("bikeRental").set(data).await()
                        data["rental"] = "Scooty"
                        fireStone.fireStoreCollection.document("scootyRental").set(data).await()
                        withContext(Dispatchers.Main){
                            startActivity(intent)
                            verifyProgressBar.visibility = View.GONE
                            verifyProgressTextView.visibility = View.GONE
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this,"UnsuccessFull..!!",Toast.LENGTH_LONG).show()
                }
            }
    }
}