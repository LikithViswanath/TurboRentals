package com.example.turborentals.ui.addRental

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.turborentals.GlobalVariables
import com.example.turborentals.PhotoActivity
import com.example.turborentals.R
import com.example.turborentals.databinding.ActivityAddRentalBinding
import com.example.turborentals.ui.profile.ProfileActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AddRentalActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val globalVariables =  (application as GlobalVariables)
        val rentalType = globalVariables.rentalType
        val factory = AddRentalViewModelFactory(rentalType,application,Firebase.auth.currentUser?.uid.toString())
        ViewModelProvider(this,factory).get(AddRentalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding :ActivityAddRentalBinding =DataBindingUtil.setContentView(this,R.layout.activity_add_rental)

        val photoActivityLeft = PhotoActivity(this)
        photoActivityLeft.phoneActivityResultLauncher = registerForActivityResult(photoActivityLeft.photoActivityResultContract){
               it?.let {
                   viewModel.imageUriLeft.value = it
               }
        }

        val photoActivityRight = PhotoActivity(this)
        photoActivityRight.phoneActivityResultLauncher = registerForActivityResult(photoActivityRight.photoActivityResultContract){
            it?.let {
                viewModel.imageUriRight.value = it
            }
        }

        val photoActivityFront = PhotoActivity(this)
        photoActivityFront.phoneActivityResultLauncher = registerForActivityResult(photoActivityFront.photoActivityResultContract){
            it?.let {
                viewModel.imageUriFront.value = it
            }
        }

        val photoActivityBack = PhotoActivity(this)
        photoActivityBack.phoneActivityResultLauncher = registerForActivityResult(photoActivityBack.photoActivityResultContract){
            it?.let {
                viewModel.imageUriBack.value = it
            }
        }

        binding.photoActivityLeft = photoActivityLeft
        binding.photoActivityRight = photoActivityRight
        binding.photoActivityFront = photoActivityFront
        binding.photoActivityBack = photoActivityBack

        binding.addRentalViewModel = viewModel
        binding.lifecycleOwner = this

        binding.cancelRentalButton.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.imageUriLeft.observe(this, Observer {
              it?.let {
                  binding.leftSideImageView.setImageURI(it)
              }
        })

        viewModel.imageUriRight.observe(this, Observer {
            it?.let {
                binding.rightSideImageView.setImageURI(it)
            }
        })

        viewModel.imageUriFront.observe(this, Observer {
            it?.let {
                binding.frontSideImageView.setImageURI(it)
            }
        })

        viewModel.imageUriBack.observe(this, Observer {
            it?.let {
                binding.backSideImageView.setImageURI(it)
            }
        })

        viewModel.error.observe(this, Observer {
            it.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

    }
}