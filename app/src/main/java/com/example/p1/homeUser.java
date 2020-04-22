package com.example.p1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;   ///////////////////////////////////////////////////////////
import android.widget.Toast;

import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class homeUser extends FragmentActivity implements  OnMapReadyCallback {

    GoogleMap map;
    //SupportMapFragment supportMapFragment;
    //SearchView searchView;
    Handler mHandler = new Handler();
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseFirestore db;
    private static final int REQUEST_CODE=101;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        db=FirebaseFirestore.getInstance();
        //searchView= findViewById(R.id.sv_location) ;
       //supportMapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        /*LatLng Delhi = new LatLng(28.612061, 77.230956);
        String location = Delhi.toString();*/

      /*  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location!=null||!location.equals("")){
                    Geocoder geocoder= new Geocoder(homeUser.this); /////////// jo bhi address enter kia usko longitude and latitude k cordinates me convert karta hai...
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
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));

                    Double lat1= address.getLatitude();  /////////////////////////// for calculating the distance
                    Double long1= address.getLongitude();

                    Location startPoint=new Location("LocationA");
                    startPoint.setLatitude(lat1);
                    startPoint.setLongitude(long1);

                    Location endPoint=new Location("locationA");
                    endPoint.setLatitude(GlobalVariable.userLat);
                    endPoint.setLongitude(GlobalVariable.userLong);

                    double distance=startPoint.distanceTo(endPoint);
                    distance=distance/1000+GlobalVariable.delPointDistance;
                    final String dist = Double.toString(distance);
                    Toast.makeText(homeUser.this,dist , Toast.LENGTH_SHORT).show();


                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Intent i = new Intent(homeUser.this,orderFoodU.class);

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
*/

    }



    private void fetchLastLocation() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        return;
        }
        Task<Location> task= fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation=location;
                    //Toast.makeText(getApplicationContext(),text:currentLocation.getLatitude()+""+currentLocation.getLongitude(),Toast.LENGTH_SHORT.show());
                    Toast.makeText(homeUser.this,currentLocation.getLatitude()+" , "+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(homeUser.this);
                    Double lat1= currentLocation.getLatitude();  /////////////////////////// for calculating the distance
                    Double long1= currentLocation.getLongitude();
                    GlobalVariable.userLat=lat1;
                    GlobalVariable.userLong=long1;
                    db.collection("RiderLat")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //this code will execute when data is completely extracted.
                                    if(task.isSuccessful()){
                                        //Let's make our database final:
                                        QuerySnapshot document = task.getResult();
                                        if(!document.isEmpty()){
                                            GlobalVariable.receiverLat=document.getDocuments().toString();
                                        }
                                    }
                                }
                            });

                    db.collection("RiderLong")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //this code will execute when data is completely extracted.
                                    if(task.isSuccessful()){
                                        //Let's make our database final:
                                        QuerySnapshot document = task.getResult();
                                        if(!document.isEmpty()){
                                            GlobalVariable.receiverLong=document.getDocuments().toString();
                                        }
                                    }
                                }
                            });


                    double rLat= Double.parseDouble(GlobalVariable.receiverLat);
                    double rLong = Double.parseDouble(GlobalVariable.receiverLong);

                    Location startPoint=new Location("LocationB");
                    startPoint.setLatitude(GlobalVariable.userLat);
                    startPoint.setLongitude(GlobalVariable.userLong);

                    Location endPoint=new Location("locationB");
                    endPoint.setLatitude(rLat);
                    endPoint.setLongitude(rLong);
                    double distance=startPoint.distanceTo(endPoint);
                    distance=distance/1000;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String formatted = df.format(distance);
                    distance = Double.parseDouble(formatted);
                    GlobalVariable.delPointDistance=distance;

                    final String dist = Double.toString(distance);
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Intent i = new Intent(homeUser.this,homeUserPickupPoint.class);
                            startActivity(i);

                        }

                    }, 10000L);



                }
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("you are here!!");
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13));
        googleMap.addMarker(markerOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }
}

