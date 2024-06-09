package com.example.myownchat.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import com.example.myownchat.R
import java.io.ByteArrayOutputStream

class Functions {
    companion object{
        fun getImageBase64StringFromBitmap(bitmap: Bitmap) : String{
            val maxBytes = 1048487 - 89  // Maximum allowed size for the base64 string
            var quality = 50
            var base64String: String

            do {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)

                if (base64String.toByteArray().size < maxBytes) {
                    break
                }

                quality -= 1  // Reduce quality by 5% and try again
            } while (quality > 0)

            return base64String
        }

        fun getImageBitmapFromUri(context: Context, uri: Uri): Bitmap{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else{
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }

        fun getDefaultPersonImageBase64String(context: Context): String{
            val defaultBitmap = BitmapFactory.decodeResource(
                context.resources,
                R.drawable.default_person_image
            )
            return getImageBase64StringFromBitmap(defaultBitmap)
        }

        fun getImageBitmapFromImageBase64String(base64String: String): Bitmap{
            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            return bitmap
        }
    }
}