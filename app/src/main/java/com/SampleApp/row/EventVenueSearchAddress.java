package com.SampleApp.row;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventVenueSearchAddress extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    View view;
    String TAG = "EventVenueSearchAddress";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_venue_search_address_fragment);

        getLatLon();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        mapFragment.getMapAsync(this);

        startSearch();

    }


    private void startSearch() {

        try {
            Log.d("MAP", "INTENT --- ");
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MAP", "INTENT --- ");

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                mapMove(latLng, place.getAddress().toString());

               // mapMoveSearchText(longitude,latitude,"Nashik");

                Log.d("-------------", "Place: " + place.getName());


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.d("--------------", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getLatLon() {
        latitude = 0.0;
        longitude = 0.0;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 8));
        //getAddressFromLatLong(latitude, longitude);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(this);
    }


  /*  private void mapMove(GetAddressModel model)
    {
        final Double lat =model.getResults().get(0).getGeometry().getLocation().getLat();
        final Double lon = model.getResults().get(0).getGeometry().getLocation().getLng();
        LatLng latLng = new LatLng(lat, lon);

        mMap.addMarker(new MarkerOptions().position(latLng).title(model.getResults().get(0).getFormatted_address()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        final String locationAdd = model.getResults().get(0).getFormatted_address();

        Toast.makeText(getApplicationContext(),""+locationAdd,Toast.LENGTH_LONG).show();
    }
*/
    private void mapMoveSearchText(Double latitude, Double longitude, String address) {
        final Double lat = latitude;
        final Double lon = longitude;
        LatLng latLng = new LatLng(lat, lon);
        MarkerOptions a = new MarkerOptions().position(latLng);
        Marker m = mMap.addMarker(a);
        mMap.addMarker(new MarkerOptions().position(latLng).title("" + address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        final String locationAdd = "" + address;
        //  startSearch();
        Log.d("TOUCHBASE", "MAP--Search Address-" + lat + "-" + lon);
        Toast.makeText(getApplicationContext(), "" + locationAdd, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("address", locationAdd);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        setResult(RESULT_OK, intent);
        finish();//finishing activity

    }

//====================================Used ====================
    private void mapMove(final LatLng latLng, String address) {
        MarkerOptions a = new MarkerOptions()
                .position(latLng);

        mMap.addMarker(new MarkerOptions().position(latLng).title("" + address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        final String locationAdd = "" + address;
        Log.d("TOUCHBASE", "MAP--Search Address-" + address);
        Toast.makeText(getApplicationContext(), "" + locationAdd, Toast.LENGTH_SHORT).show();


        Intent intent = new Intent();
        intent.putExtra("address", locationAdd);
        setResult(5, intent);
        finish();//finishing activity

    }

    //=========================================================================

    public void onMapLongClick(final LatLng point) {
        Log.d("MAP", "POINTS -" + point.toString());
        getAddressFromLatLong(point.latitude, point.longitude);
    }

    private String getAddressFromLatLong(double latitude, double longitude) {
        Log.d("TOUCHBAE", "LAT LONG:-" + latitude + " -- " + longitude);
        String address = "";
        try {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            Log.d("TOUCHBAE", "Address lenght" + addresses.isEmpty());
            if (!addresses.isEmpty()) {
                address = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getAddressLine(1);//+","+addresses.get(0).getAddressLine(3);
                Log.d("TOUCHBAE", "ADDRESS ---" + address + " -- " + addresses.get(0).getAddressLine(3));

                String city = addresses.get(0).getLocality();
                Log.d("=============","========"+city);
                String state = addresses.get(0).getAdminArea();
                Log.d("=============","========"+state);

            }
                mapMoveSearchText(latitude, longitude, address);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}


