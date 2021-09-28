package com.NEWROW.row;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.NEWROW.row.Utils.Constant;
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


public class SearchAddress extends AppCompatActivity implements OnMapReadyCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        //      initToolbar();
        getLatLon();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        view =this.getWindow().getDecorView().findViewById(android.R.id.content);
        mapFragment.getMapAsync(this);

        tv_done = (TextView)findViewById(R.id.tv_done);
        tv_done.setVisibility(View.VISIBLE);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("Get Direction");
        tv_title.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        iv_backbutton = (ImageView)findViewById(R.id.iv_backbutton);
        iv_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });*/
    }

    private void startSearch() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                LatLng latLng = place.getLatLng();
                mapMove(latLng,place.getAddress().toString());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getLatLon() {

        Intent intent = getIntent();
        if (intent != null) {


            latitude = intent.getDoubleExtra(Constant.LATITUDE, 0.0);
            longitude = intent.getDoubleExtra(Constant.LONGITUDE, 0.0);
        }
    }

 /*   private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_white);
        toolbar.setTitle(R.string.events_address);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_share_location);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.search_location) {
                    startSearch();
                }
                return false;
            }
        });
    }
*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("Here", "Map is ready now");
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
              /*  options.icon(BitmapDescriptorFactory.fromResource(R.drawable.app_icon));*/
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
        });
    }

    private void mapMove(GetAddressModel model)
    {
        final Double lat =model.getResults().get(0).getGeometry().getLocation().getLat();
        final Double lon = model.getResults().get(0).getGeometry().getLocation().getLng();
        LatLng latLng = new LatLng(lat, lon);
        final String locationAdd = model.getResults().get(0).getFormatted_address();

        Snackbar.make(fab,locationAdd, Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.LOCATION,locationAdd);
                        bundle.putDouble(Constant.LATITUDE,lat);
                        bundle.putDouble(Constant.LONGITUDE,lon);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).show();

        mMap.addMarker(new MarkerOptions().position(latLng).title(model.getResults().get(0).getFormatted_address()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }

    private void mapMove(final Double latitude, final Double longitude, String address)
    {
        final LatLng latLng = new LatLng(latitude, longitude);
        final String locationAdd = ""+address;
       /* Snackbar.make(fab,locationAdd.toString(), Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.LOCATION,locationAdd);
                        bundle.putDouble(Constant.LATITUDE,latitude);
                        bundle.putDouble(Constant.LONGITUDE,longitude);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).show();*/

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.LOCATION,locationAdd);
                bundle.putDouble(Constant.LATITUDE,latitude);
                bundle.putDouble(Constant.LONGITUDE,longitude);

                Log.d("@@@@@@@@.........","@@@@@@@@@@"+longitude);
                Log.d("@@@@@@@@","@@@@@@@@@@"+latitude);

                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        MarkerOptions a = new MarkerOptions()
                .position(latLng);
        marker = mMap.addMarker(a);
        mMap.addMarker(new MarkerOptions().position(latLng).title(""+address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));



        mMap.clear();
        MarkerOptions a1 = new MarkerOptions()
                .position(latLng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(""+address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        final String locationAdd1 = ""+address;
        Snackbar.make(fab,locationAdd + latitude+"  "+longitude, Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.LOCATION,locationAdd);
                        bundle.putDouble(Constant.LATITUDE,latLng.latitude);
                        bundle.putDouble(Constant.LONGITUDE,latLng.longitude);

                        Log.d("@@@@@@@@.........","@@@@@@@@@@"+longitude);
                        Log.d("@@@@@@@@","@@@@@@@@@@"+latitude);

                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }).show();


    }


    private void mapMove(final LatLng latLng, String address)
    {
        mMap.clear();
        MarkerOptions a = new MarkerOptions()
                .position(latLng);
        mMap.addMarker(new MarkerOptions().position(latLng).title(""+address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        final String locationAdd = ""+address;
        Snackbar.make(fab,locationAdd + latitude+"  "+longitude, Snackbar.LENGTH_INDEFINITE)
                .setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.LOCATION,locationAdd);
                        bundle.putDouble(Constant.LATITUDE,latLng.latitude);
                        bundle.putDouble(Constant.LONGITUDE,latLng.longitude);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);

                       /* Log.d("@@@@@@@@.........","@@@@@@@@@@"+longitude);
                        Log.d("@@@@@@@@","@@@@@@@@@@"+latitude);*/

                        finish();
                    }
                }).show();
    }

   /* public void onMapLongClick(final LatLng point) {
        getAddressFromLatLong(point.latitude,point.longitude);
     }*/


    private String getAddressFromLatLong(double latitude, double longitude)
    {
        mMap.clear();
        String address ="";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0)+","+addresses.get(0).getAddressLine(1);
            mapMove(latitude,longitude,address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    private void openDialog(){
        LayoutInflater inflater = LayoutInflater.from(SearchAddress.this);
        View subView = inflater.inflate(R.layout.dg_manual_location, null);
        final EditText subEditText = (EditText)subView.findViewById(R.id.dialogEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter event address");
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.LOCATION,subEditText.getText().toString());
                bundle.putDouble(Constant.LATITUDE,0.0);
                bundle.putDouble(Constant.LONGITUDE,0.0);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                hideSoftKeyboard();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
