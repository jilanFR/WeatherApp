package com.example.weatherapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change the background color of ActionBar
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(Color.parseColor("#2580B3"))
        )

        // set button to shift on classes
        binding.mainButton.setOnClickListener {

            var text = binding.mainEditText.text.toString()
            val secondActivityIntent = Intent(this, MainPage::class.java)
            secondActivityIntent.putExtra("zipcode",text)
            startActivity(secondActivityIntent)
            }
    }
}