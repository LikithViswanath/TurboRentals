package com.example.turborentals

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.theartofdev.edmodo.cropper.CropImage

class PhotoActivity( val context: Context ) {
    val photoActivityResultContract =
        object : ActivityResultContract<Any?, Uri?>() {
            override fun createIntent(context: Context, input: Any?): Intent {
                return CropImage.activity()
                    .setAspectRatio(16,9)
                    .getIntent(context)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return CropImage.getActivityResult(intent)?.uri
            }

        }
    lateinit var phoneActivityResultLauncher : ActivityResultLauncher<Any?>
}