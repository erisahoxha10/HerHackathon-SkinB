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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

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


        switch_location.setOnClickListener {
            val isChecked = switch_location.isChecked

            if(isChecked) {
                var location_here : Location = Location("dummyprovider");

                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location: Location? ->
                    // Got last known location. In some rare
                    // situations this can be null.
                    if (location == null) {
                        // TODO, handle it
                        println("Location is null")
                    } else location.apply {
                        // Handle location object
                        println(location.latitude)
                        location_here = location
                        Toast.makeText(this@MainActivity,
                            "Checked!" + location_here.latitude + " " + location_here.longitude,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


            }else{
                Toast.makeText(this, "Not checked",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

}