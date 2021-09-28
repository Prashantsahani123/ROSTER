package com.NEWROW.row;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.NEWROW.row.Utils.Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by USER1 on 03-08-2016.
 */
public class ViewAddress extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private Double latitude=0.0;
    private Double longitude=0.0;
    View view;
    String TAG="SearchAddress";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    FloatingActionButton fab;
    Marker marker;
    TextView tv_done;
    int detailFlag;


    TextView tv_title;
    ImageView iv_backbutton;
    private ImageView iv_actionbtn;
    String address,cityGoogleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        //      initToolbar();
        getLatLon();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        mapFragment.getMapAsync(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        iv_actionbtn = (ImageView) findViewById(R.id.iv_actionbtn);
        iv_actionbtn.setVisibility(View.INVISIBLE);
        // iv_backbutton.setVisibility(View.GONE);
        // tv_title.setText("Directory");
        tv_title.setText(address);

        tv_done = (TextView) findViewById(R.id.tv_done);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDialog();

               /* Uri uri = Uri.parse("geo: "+latitude+","+longitude);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);*/

                if(address.trim().length() != 0) {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + address));
                    startActivity(intent);*/
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude));
                    startActivity(intent);


                }else{
                    Toast.makeText(ViewAddress.this, "Address details not entered", Toast.LENGTH_LONG).show();
                }

            }
        });


        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getLatLon() {

        Intent intent = getIntent();
        if (intent != null) {


            latitude = intent.getDoubleExtra(Constant.LATITUDE, 0.0);
            longitude = intent.getDoubleExtra(Constant.LONGITUDE, 0.0);
            address = intent.getStringExtra("address");

            //cityGoogleMap = intent.getStringExtra("city");
           // Log.d("-----------","---add---"+cityGoogleMap);


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setOnMapLongClickListener(this);
       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (marker != null) {
                    marker.remove();
                }
                getAddressFromLatLong(point.latitude,point.longitude);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions options = new MarkerOptions();
              *//*  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.app_icon));*//*
                options.position(latLng);
                options.draggable(true);
                mMap.addMarker(options);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Log.d("=============","====Long========="+longitude);
                Log.d("=============","====Long========="+latitude);

            }
        });*/

    }
}

