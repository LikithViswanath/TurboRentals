package com.example.turborentals.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.turborentals.R
import com.example.turborentals.ui.profile.ProfileActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreen)

        val auth = Firebase.auth
        val currentUser = auth.currentUser?.uid

        if(currentUser!=null){
            val intent = Intent(this, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        setTheme(R.style.TurboStyle)
        setContentView(R.layout.activity_main)

        ccp.registerCarrierNumberEditText(t1)
        b1.setOnClickListener {
            val intent = Intent(this,OtpActivity::class.java)
            intent.putExtra("phoneNumber",ccp.fullNumberWithPlus)
            startActivity(intent)
        }

    }

}
