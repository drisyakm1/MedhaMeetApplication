package com.example.mymapapplication.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymapapplication.R;
import com.example.mymapapplication.models.Company;
import com.example.mymapapplication.viewmodels.CompanyViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private CompanyViewModel companyViewModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    private ArrayList<Company> companyList = new ArrayList<>();
    private ArrayList<Company> nearByCompanyList = new ArrayList<>();
    private CompanyRecyclerAdapter adapter;
    Location currentLocation = new Location("Point A");
    Marker prevMarker;
    int PrevMarkerPos;
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        sharedPreferences = getSharedPreferences(
//                getString(R.string.app_name), Context.MODE_PRIVATE);

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize BottomSheet
        initBottomSheet();
        //Location Updates
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.i("MainActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    moveMap(location.getLatitude(), location.getLongitude());
                    companyViewModel.init(location.getLatitude(), location.getLongitude(), false);
                    companyViewModel.getCompanyRepository().observe(MapsActivity.this, companyResponse -> {
                        if (companyResponse.size() > 0) {
                            companyList = companyResponse;
                            currentLocation.setLatitude(location.getLatitude());
                            currentLocation.setLongitude(location.getLongitude());
                            moveMap(location.getLatitude(), location.getLongitude());
//                            if(companyList.size() >0) {
                            mMap.clear();
                            moveMap(location.getLatitude(), location.getLongitude());
                            updateMarkers(companyList);
//                            }
                        }
                    });
//                    }


                }
            }

            ;

        };


    }



    @Override
    protected void onResume() {
        super.onResume();
        if (mFusedLocationClient != null) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (mFusedLocationClient != null) {
            requestLocationUpdates();
        }
    }


//    private void saveCurrentLocation() {
//
//    }

    private void initBottomSheet() {
        CardView cvBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(cvBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void updateMarkers(ArrayList<Company> companyList) {
        if (companyList != null) {
            for (int i = 0; i < companyList.size(); i++) {
                LatLng latLng = new LatLng(companyList.get(i).getLatitude(), companyList.get(i).getLongitude());

                IconGenerator factory = new IconGenerator(this);
                factory.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));

                View inflatedView = View.inflate(this, R.layout.marker_custom, null);
                ImageView imageView = inflatedView.findViewById(R.id.image_marker);
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                TextView markerText = inflatedView.findViewById(R.id.text_marker);
                markerText.setText(companyList.get(i).getAvgRating().toString());
                factory.setContentView(inflatedView);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(factory.makeIcon()));
//                        .snippet(companyList.get(i).getAvgRating().toString())
//                        .title(companyList.get(i).getAvgRating().toString());
                Marker marker = mMap.addMarker(markerOptions);

                mHashMap.put(marker, i);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }

            if (companyList != null && companyList.size() > 0) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                bindSheet(companyList.get(0));
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mHashMap.get(marker) != null) {
                    int pos = mHashMap.get(marker);

                    if (prevMarker != null) {
                        IconGenerator factory = new IconGenerator(MapsActivity.this);
                        factory.setBackground(ContextCompat.getDrawable(MapsActivity.this, android.R.color.transparent));

                        View inflatedView = View.inflate(MapsActivity.this, R.layout.marker_custom, null);
                        ImageView imageView = inflatedView.findViewById(R.id.image_marker);
                        imageView.setColorFilter(ContextCompat.getColor(MapsActivity.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                        TextView markerText = inflatedView.findViewById(R.id.text_marker);
                        markerText.setText(companyList.get(PrevMarkerPos).getAvgRating().toString());
                        factory.setContentView(inflatedView);
                        //Set prevMarker back to default color
                        prevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(factory.makeIcon()));
                    }

                    //leave Marker default color if re-click current Marker
                    if (!marker.equals(prevMarker)) {
                        IconGenerator factory = new IconGenerator(MapsActivity.this);
                        factory.setBackground(ContextCompat.getDrawable(MapsActivity.this, android.R.color.transparent));

                        View inflatedView = View.inflate(MapsActivity.this, R.layout.marker_custom, null);
                        ImageView imageView = inflatedView.findViewById(R.id.image_marker);
                        imageView.setColorFilter(ContextCompat.getColor(MapsActivity.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                        TextView markerText = inflatedView.findViewById(R.id.text_marker);
                        markerText.setText(companyList.get(pos).getAvgRating().toString());
                        factory.setContentView(inflatedView);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(factory.makeIcon()));
                        prevMarker = marker;
                        PrevMarkerPos = pos;
                    }
                    prevMarker = marker;
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bindSheet(companyList.get(pos));

                }
                return false;
            }

        });

    }

    private void bindSheet(Company company) {
        TextView txtCompanyName = findViewById(R.id.company_name);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView txtRating = findViewById(R.id.company_rating);
        TextView txtDistance = findViewById(R.id.company_distance);
        ImageView imageCompany = findViewById(R.id.company_img);
        TextView txtCompanyDescription = findViewById(R.id.company_description);
        txtCompanyName.setText(company.getCompanyName());
        txtRating.setText(String.valueOf(company.getAvgRating()));
        ratingBar.setRating(company.getAvgRating());
        txtCompanyDescription.setText(company.getCompanyDescription());
        Glide.with(this).load(company.getCompanyImageUrl()).into(imageCompany);

        // Distance Calculation
//        Location locationB = new Location("point B");
//
//        locationB.setLatitude(company.getLatitude());
//        locationB.setLongitude(company.getLongitude());
//
//        float distance = currentLocation.distanceTo(locationB);
//        txtDistance.setText(String.valueOf(distance));

        RecyclerView recyclerView = findViewById(R.id.rv_companyList);
        adapter = new CompanyRecyclerAdapter(this, nearByCompanyList);
        recyclerView.setAdapter(adapter);

        companyViewModel.init(company.getLatitude(), company.getLongitude(), true);
        companyViewModel.getNearByCompanyRepository().observe(MapsActivity.this, new Observer<ArrayList<Company>>() {
            @Override
            public void onChanged(ArrayList<Company> companies) {
//                if(companies.size() > 0) {
                ArrayList<Company> test = new ArrayList<>();
                for (Company company1 : companies) {
                    if (company.getCompanyId() != company1.getCompanyId()) {
                        test.add(company1);
                    }

                }
                adapter.updateList(test);
//                }


            }
        });

    }

    private void moveMap(double latitude, double longitude) {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding current location marker to map
         * move the camera with animation
         */
        LatLng latLng = new LatLng(latitude, longitude);
//        Toast.makeText(this, "currentLocation: " + latitude + "," + longitude, Toast.LENGTH_LONG).show();
        IconGenerator factory = new IconGenerator(this);
        factory.setBackground(ContextCompat.getDrawable(this, android.R.color.transparent));

        View inflatedView = View.inflate(this, R.layout.marker_custom, null);
        ImageView imageView = inflatedView.findViewById(R.id.image_marker);
        ((ImageView) inflatedView.findViewById(R.id.img_grade)).setVisibility(View.GONE);
        imageView.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView markerText = inflatedView.findViewById(R.id.text_marker);
        markerText.setText("You");
        factory.setContentView(inflatedView);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(factory.makeIcon())));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000); // two minute interval
        mLocationRequest.setFastestInterval(600000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            checkLocationPermission();
        }
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {


                        mMap.setMyLocationEnabled(true);
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {

                                        if (location != null) {

                                            //    local=findViewById(R.id.tv5);

                                            double la = location.getLatitude();
                                            double lo = location.getLongitude();

                                            LatLng curre = new LatLng(la, lo);
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curre, 18));

                                        }
                                    }
                                });
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

}