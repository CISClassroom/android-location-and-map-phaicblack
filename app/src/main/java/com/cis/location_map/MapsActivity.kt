package com.cis.location_map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var locationClient : FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    var lat:Double = 0.0
    var lon:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(lat,lon)
        mMap.addMarker(MarkerOptions().position(sydney).title("Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getLocation() {
        if(checkPermission()){
            locationClient.lastLocation.addOnCompleteListener(this) {task ->
                var location : Location? = task.result
                if(location != null){
                    lat = location.latitude.toDouble() ;
                    lon = location.longitude.toDouble();
                    onMapReady(mMap)
                }
            }
        }else{
            requestPermissions()
            Log.i("Location","null location")
        }
    }
    private fun checkPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            )== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            101
        )
    }
}
