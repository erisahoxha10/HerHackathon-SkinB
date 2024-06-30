package com.example.kothackathon

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL

class MainActivity : Activity() {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var text: TextView
    private lateinit var switch_location: Switch

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        switch_location = findViewById(R.id.switch_location)
        title = "SkinB"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // when location permission is enabled, we can get lat and long of position and use that info
        // to get uvIndex, temperature, humidity
        switch_location.setOnClickListener {
            val isChecked = switch_location.isChecked

            if(isChecked) {

                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location? ->
                    // Got last known location. In some rare
                    // situations this can be null.
                    if (location == null) {
                        // TODO, handle it
                        //println("Location is null")
                    } else location.apply {
                        // Handle location object

                        // Instantiate the RequestQueue.
                        val queue = Volley.newRequestQueue(this@MainActivity)
                        val url = "https://www.uvindex.app/api/getUvTimeline?lat=49.4737694&lng=8.4599115"

                        // Request a string response from the provided URL.
                        val stringRequest = StringRequest(
                            Request.Method.GET, url,
                            Response.Listener<String> { response ->
                                // Display the first 500 characters of the response string.
                                //textView.text = "Response is: ${response.substring(0, 500)}"
                                var text = "Response is: ${response.substring(0, 500)}"
                                println(text)
                                val answer = JSONObject(response)
                                var uvIndexNow = answer.get("uvIndexNow")
                                var humidityNow = answer.get("humidityNow")
                                var temperatureNow = answer.get("temperatureNow")
                                println(uvIndexNow)

                                Toast.makeText(this@MainActivity,
                                    "Location enabled",
                                    Toast.LENGTH_LONG
                                ).show()

                            },
                            Response.ErrorListener { println("That didn't work!") })

                        // Add the request to the RequestQueue.
                        queue.add(stringRequest)

                    }
                }

            }else{
                Toast.makeText(this, "Location disabled",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

}