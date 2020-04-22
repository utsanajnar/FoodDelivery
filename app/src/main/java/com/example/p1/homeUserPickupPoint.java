package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class homeUserPickupPoint extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SupportMapFragment supportMapFragment;
    SearchView searchView;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user_pickup_point);

        searchView= findViewById(R.id.sv_location) ;
        supportMapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location!=null||!location.equals("")){
                    Geocoder geocoder= new Geocoder(homeUserPickupPoint.this); /////////// jo bhi address enter kia usko longitude and latitude k cordinates me convert karta hai...
                    try{
                        addressList= geocoder.getFromLocationName(location ,1);
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    assert addressList != null;
                    Address address=addressList.get(0);
                    LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());  //LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));

                    Double lat1= address.getLatitude();  /////////////////////////// for calculating the distance
                    Double long1= address.getLongitude();

                    Location startPoint=new Location("LocationA");
                    startPoint.setLatitude(lat1);
                    startPoint.setLongitude(long1);

                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(GlobalVariable.userLat);
                    endPoint.setLongitude(GlobalVariable.userLong);

                    double distance=startPoint.distanceTo(endPoint);
                    distance=distance/1000;
                    final String dist = Double.toString(distance);
                    Toast.makeText(homeUserPickupPoint.this,dist , Toast.LENGTH_SHORT).show();


                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Intent i = new Intent(homeUserPickupPoint.this,orderFoodU.class);

                            i.putExtra("key",dist);
                            startActivity(i);
                        }

                    }, 10000L);




                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
    }
}
