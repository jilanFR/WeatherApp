package com.example.weatherapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.MainPageBinding
import kotlinx.android.synthetic.main.main_page.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainPage  : AppCompatActivity() {

    // Value of the search query to find weather information of a specific place
    var ZIP: String? = "90210"

    // Using API key
    val API: String = "38d246c54ae6fdd0595905423a024ebb"

    // Request weather information using CITY & API information
    var url = URL("https://api.openweathermap.org/data/2.5/weather?q=$ZIP&units=metric&appid=$API")

    lateinit var  binding : MainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////////////////////////////////////////
        ////////////////ANIMATION CREATION//////////////////
        ////////////////////////////////////////////////////
        val ttb =AnimationUtils.loadAnimation(this,R.anim.ttb)
        overviewContainer.startAnimation(ttb)

        val stb =AnimationUtils.loadAnimation(this,R.anim.stb)
        addressContainer.startAnimation(stb)

       val btt =AnimationUtils.loadAnimation(this,R.anim.btt)
        status.startAnimation(btt)

        val course1 =AnimationUtils.loadAnimation(this,R.anim.course1)
        object1.startAnimation(course1)

        val course2 =AnimationUtils.loadAnimation(this,R.anim.course2)
        object2.startAnimation(course2)

        val course3 =AnimationUtils.loadAnimation(this,R.anim.course3)
        object3.startAnimation(course3)

        val course4 =AnimationUtils.loadAnimation(this,R.anim.course4)
        object4.startAnimation(course4)

        val course5 =AnimationUtils.loadAnimation(this,R.anim.course5)
        object5.startAnimation(course5)

        val course6 =AnimationUtils.loadAnimation(this,R.anim.course6)
        object6.startAnimation(course6)
        /////////////////////////////////////////////////////
        ////////////////////////////////////////////////////
        ////////////////////////////////////////////////////

        // User input
        var test = intent.getStringExtra("zipcode")

        //ZIP = test
        var url = URL("https://api.openweathermap.org/data/2.5/weather?q=$test&units=metric&appid=$API")
        weatherTask(url)
    }

    // create coroutine
    fun weatherTask(url: URL) {

        CoroutineScope(Dispatchers.IO).launch {
            var response: String?

            // using try blocks as safeguards against crashes
            try {
                response =url.readText()

                val data = async { updateAdviceText(response!!) }.await()
                //Log.d("ZIPCODE", url.readText())
            } catch (e: Exception) {
                response = null
            }
        }
    }

    suspend fun updateAdviceText(result: String) {

        // try to fetch data
        try {
            /* Extracting JSON returns from the API */
            val jsonObj = JSONObject(result)

            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val wind_ = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
            val updatedAt: Long = jsonObj.getLong("dt")
            val updatedAtText = "Updated at: " + SimpleDateFormat( "dd/MM/yyyy hh:mm a", Locale.ENGLISH)
                .format( Date(updatedAt * 1000))
            val temp_ = main.getString("temp") + "°C"
            val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
            val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
            val pressure_ = main.getString("pressure")
            val humidity_ = main.getString("humidity")
            val sunrise_ : Long = sys.getLong("sunrise")
            val sunset_ : Long = sys.getLong("sunset")
            val windSpeed = wind_.getString("speed")
            val weatherDescription = weather.getString("description")
            val address = jsonObj.getString("name") + ", " + sys.getString("country")

            // whats in the background make visible in the view (moves to the main thread)
            withContext(Dispatchers.Main){

                binding.apply {
                    status.text = weatherDescription.capitalize()
                    tv_current_location.text = address
                    updated_at.text = updatedAtText
                    temp.text = temp_
                    temp_min.text = tempMin
                    temp_max.text = tempMax
                    sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise_ * 1000))
                    sunset.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset_ * 1000))
                    wind.text = windSpeed
                    pressure.text = pressure_
                    humidity.text = humidity_
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ops!! try again!", Toast.LENGTH_LONG).show()
        }
    }
}
