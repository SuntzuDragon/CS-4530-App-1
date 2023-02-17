package com.example.cs4530app1v3

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmapOrNull
import java.io.ByteArrayOutputStream
import java.lang.Boolean.TRUE
import java.lang.Boolean.FALSE

class MainActivity : AppCompatActivity() {
    private lateinit var firstNameEditText: EditText
    private lateinit var middleNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var submitBtn: Button
    private lateinit var photoImageView: ImageView

    private var imageTaken = FALSE

    companion object {
        private const val FIRST_NAME_KEY = "first_name"
        private const val MIDDLE_NAME_KEY = "middle_name"
        private const val LAST_NAME_KEY = "last_name"
        private const val IMAGE_EXISTS_KEY = "image_exists_bool"
        private const val IMAGE_KEY = "image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNameEditText = findViewById(R.id.firstNameEditText)
        middleNameEditText = findViewById(R.id.middleNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        submitBtn = findViewById(R.id.submitBtn)
        photoImageView = findViewById(R.id.photoImageView)

        submitBtn.setOnClickListener {
            if (imageTaken) {
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()

                val image = (photoImageView.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("name", "$firstName $lastName")
                intent.putExtra("image", byteArray)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Please Set a Profile Picture", Toast.LENGTH_SHORT).show()
            }
        }

        photoImageView.setOnClickListener {
            val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            camActivity.launch(camIntent)
        }
    }

    private val camActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK) {
            val thumbnailImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data!!.getParcelableExtra("data", Bitmap::class.java)
            } else {
                result.data!!.getParcelableExtra("data")
            }
            photoImageView.setImageBitmap(thumbnailImage)
            imageTaken = TRUE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(FIRST_NAME_KEY, firstNameEditText.text.toString())
        outState.putString(MIDDLE_NAME_KEY, middleNameEditText.text.toString())
        outState.putString(LAST_NAME_KEY, lastNameEditText.text.toString())
        outState.putBoolean(IMAGE_EXISTS_KEY, imageTaken)

        val imageView: ImageView = photoImageView
        val bitmap: Bitmap? = imageView.drawable?.toBitmapOrNull()
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            outState.putByteArray(IMAGE_KEY, byteArray)
            Log.d("UserImage", "NOT NULL IMAGE")
        }
        else {
            Log.d("UserImage", "NULL IMAGE")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        firstNameEditText.setText(savedInstanceState.getString(FIRST_NAME_KEY))
        middleNameEditText.setText(savedInstanceState.getString(MIDDLE_NAME_KEY))
        lastNameEditText.setText(savedInstanceState.getString(LAST_NAME_KEY))
        imageTaken = savedInstanceState.getBoolean(IMAGE_EXISTS_KEY)

        if (imageTaken) {
            val byteArray: ByteArray = savedInstanceState.getByteArray(IMAGE_KEY)!!
            photoImageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
        }
    }
}