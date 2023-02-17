package com.example.cs4530app1v3

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SecondActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var photoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        nameTextView = findViewById(R.id.nameTextView)
        photoImageView = findViewById(R.id.photoImageView)

        val name = intent.getStringExtra("name")
        val byteArray = intent.getByteArrayExtra("image")
        val imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        nameTextView.text = "$name is logged in!"
        photoImageView.setImageBitmap(imageBitmap)
    }
}