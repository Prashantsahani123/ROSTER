package com.SampleApp.row;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Adapter.CountrySpAdapter;
import com.SampleApp.row.Data.CountryData;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 02-05-2017.
 */

public class FindAClubActivity extends Activity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private FusedLocationProviderApi fusedApi = LocationServices.FusedLocationApi;
    //private GPSTracker gpsTracker;
    private ArrayList<CountryData> countryList;
    private CountrySpAdapter countryAdapter;
    LocationManager locationManager;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private static String ROW_USERNAME = "xWebROW", ROW_PASSWORD = "eG)aR>Y~=G[vmw3>";
    private LinearLayout linear_anyClub, linear_near_me, ll_clubform, ll_near_me_form;
    private TextView tv_title, tv_anyclub, tv_near_me;
    private ImageView iv_backbutton;
    private View view_any_club, view_near_me;
    String authtoken = "";
    String TAG = "FindAClubActivity";
    String meetingday[] = {"Any", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    String meetingtime[] = {"Any", "Sunrise", "Noon", "Sunset"};
    Spinner spinner_meetingday_anyclub;
    Spinner spinner_meetingday_nearme;
    Spinner spinner_meeting_time;

    EditText etName, etState, etDistrict;
    Spinner spCountry;
    TextView tvSearch;
    Context context;
    boolean isAnyClubSelected = true;// variable defined to see which tab is selected(AnyClub or NearMe)
    private EditText edt_range;
    RadioGroup rd_group;
    public int radioSelectedId;
    public String radioSelectedValue = "Kilometers";
    String meetingDay = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_club);
        context = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        actionbarfunction();
        init();
        setSpinnerMeetingDay_anyClub();
        setSpinnerMeetingDay_nearMe();
        setSpinnerMeetingTime();

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    private void setSpinnerMeetingDay_anyClub() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_meetingday, R.id.textView, meetingday);
        spinner_meetingday_anyclub.setAdapter(spinnerArrayAdapter);
    }

    private void setSpinnerMeetingDay_nearMe() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_meetingday, R.id.textView, meetingday);
        spinner_meetingday_nearme.setAdapter(spinnerArrayAdapter);
    }

    private void setSpinnerMeetingTime() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_meetingday, R.id.textView, meetingtime);
        spinner_meeting_time.setAdapter(spinnerArrayAdapter);
    }

    public void getResult(String result) {
        JSONObject json = null;
        try {
            json = new JSONObject(result);
            authtoken = json.getString("auth_token");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ROW ", "Error is" + e.toString());
        }
    }

    private void init() {
        linear_anyClub = (LinearLayout) findViewById(R.id.ll_anyclub);
        linear_near_me = (LinearLayout) findViewById(R.id.ll_near_me);
        tv_anyclub = (TextView) findViewById(R.id.tv_anyclub);
        tv_near_me = (TextView) findViewById(R.id.tv_near_me);
        view_any_club = (View) findViewById(R.id.view_any_club);
        view_near_me = (View) findViewById(R.id.view_near_me);
        spinner_meetingday_anyclub = (Spinner) findViewById(R.id.spinner_meetingday_anyclub);
        spinner_meetingday_nearme = (Spinner) findViewById(R.id.spinner_meetingday_nearme);
        spinner_meeting_time = (Spinner) findViewById(R.id.spinner_meetingtime_nearme);
        tv_anyclub.setTextColor(getResources().getColor(R.color.view_color_blue));
        view_any_club.setBackgroundColor(getResources().getColor(R.color.view_color_blue));

        edt_range = (EditText) findViewById(R.id.edt_range);
        rd_group = (RadioGroup) findViewById(R.id.radio_group_distance);

        ll_clubform = (LinearLayout) findViewById(R.id.ll_clubform);
        ll_near_me_form = (LinearLayout) findViewById(R.id.ll_near_me_form);
        ll_near_me_form.setVisibility(View.GONE);

        rd_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioSelectedId = group.getCheckedRadioButtonId();
                if (radioSelectedId == R.id.radio_miles) {
                    radioSelectedValue = "Miles";
                } else {
                    radioSelectedValue = "Kilometers";
                }
            }
        });


        linear_anyClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_anyclub.setTextColor(getResources().getColor(R.color.view_color_blue));
                view_any_club.setVisibility(View.VISIBLE);
                tv_near_me.setTextColor(getResources().getColor(R.color.black));
                view_near_me.setVisibility(View.GONE);
                ll_clubform.setVisibility(View.VISIBLE);
                ll_near_me_form.setVisibility(View.GONE);
                isAnyClubSelected = true;

            }
        });

        linear_near_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindAClubActivity.this, GPSTracker.class);
                startService(intent);

                tv_near_me.setTextColor(getResources().getColor(R.color.view_color_blue));
                view_near_me.setVisibility(View.VISIBLE);
                tv_anyclub.setTextColor(getResources().getColor(R.color.black));
                view_any_club.setVisibility(View.GONE);
                ll_clubform.setVisibility(View.GONE);
                ll_near_me_form.setVisibility(View.VISIBLE);
                isAnyClubSelected = false;
            }
        });

        //EditText etName, etState,
        etDistrict = (EditText) findViewById(R.id.et_district);
        etState = (EditText) findViewById(R.id.et_state);
        etName = (EditText) findViewById(R.id.et_name);
        spCountry = (Spinner) findViewById(R.id.spinner_country);
        tvSearch = (TextView) findViewById(R.id.tvsearch);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAClub(tvSearch);
            }
        });

        countryList = Utils.getCountryList(context);
        countryAdapter = new CountrySpAdapter(context, R.layout.select_country_item_sp, countryList, false);
        spCountry.setAdapter(countryAdapter);
    }


    public void findAClub(View view) {
        if (isAnyClubSelected) {
            if (!isValidForm()) {
                Toast.makeText(context, "Please enter value of at least one field to search for result", Toast.LENGTH_LONG).show();
            } else {
                if (InternetConnection.checkConnection(getApplicationContext())) {

                    String keyword = etName.getText().toString();
                    String country = countryList.get(spCountry.getSelectedItemPosition()).getCountryId();
                    String stateProvinceCity = etState.getText().toString();
                    String district = etDistrict.getText().toString();
                    meetingDay = spinner_meetingday_anyclub.getSelectedItem().toString();
                    if (meetingDay.equalsIgnoreCase("Any")) {
                        meetingDay = "";
                    }

                    Intent intent = new Intent(context, FindAClubResultActivity.class);
                    intent.putExtra("keyword", keyword);
                    intent.putExtra("country", country);
                    intent.putExtra("stateProvinceCity", stateProvinceCity);
                    intent.putExtra("district", district);
                    intent.putExtra("meetingDay", meetingDay);
                    intent.putExtra("tabSelected", "Any Club");
                    //intent.putExtra("auth_token", authtoken);
                    // intent.putExtra("searchterm", params);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (InternetConnection.checkConnection(getApplicationContext())) {

                final ProgressDialog dialog = new ProgressDialog(FindAClubActivity.this);
                dialog.setMessage("Please wait, checking your current location...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);

                dialog.setIndeterminate(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMax(100);

                dialog.show();

                Handler handler = new Handler() {
                    int ctr = 0;

                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        try {
                            String latitude = String.valueOf(mCurrentLocation.getLatitude());
                            String longitude = String.valueOf(mCurrentLocation.getLongitude());

                            if (latitude.equalsIgnoreCase("0.0") && longitude.equalsIgnoreCase("0.0")) {
                                Utils.log("Lat : " + latitude + " Longi : " + longitude);
                                ctr++;
                                sendEmptyMessageDelayed(0, 1000);

                            } else {

                                dialog.dismiss();
                                String distance = edt_range.getText().toString();
                                if (distance.equalsIgnoreCase("") || distance.trim().equalsIgnoreCase("")) {
                                    distance = "10";// default value should be 10
                                }
                                meetingDay = spinner_meetingday_nearme.getSelectedItem().toString();
                                if (meetingDay.equalsIgnoreCase("Any")) {
                                    meetingDay = "";
                                }

                                String meetingTime = spinner_meeting_time.getSelectedItem().toString();
                                if (meetingTime.equalsIgnoreCase("Any")) {
                                    meetingTime = "";
                                } else {
                                    meetingTime = String.valueOf(spinner_meeting_time.getSelectedItemId());
                                }

                                Intent intent = new Intent(context, FindAClubResultActivity.class);
                                intent.putExtra("distance", distance);
                                intent.putExtra("distanceUnit", radioSelectedValue);
                                intent.putExtra("meetingDay", meetingDay);
                                intent.putExtra("meetingTime", meetingTime);
                                intent.putExtra("currentLat", latitude);
                                intent.putExtra("currentLong", longitude);
                                intent.putExtra("tabSelected", "Near Me");
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(FindAClubActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                            1);
                                } else {
                                    PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, FindAClubActivity.this);
                                }
                            } else {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable GPS?")
                                        .setCancelable(false)
                                        .setPositiveButton("Enable GPS",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Intent callGPSSettingIntent = new Intent(
                                                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                                        startActivity(callGPSSettingIntent);
                                                    }
                                                });
                                alertDialogBuilder.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Toast.makeText(context, "Unable to get location. You must switch on GPS to use 'Near Me' feature", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                AlertDialog alert = alertDialogBuilder.create();
                                alert.show();
                            }
                        }
                    }
                };
                handler.sendEmptyMessage(0);

            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean isValidForm() {

        if (!etName.getText().toString().trim().equals("")) {
            return true;
        }
        if (!etState.getText().toString().trim().equals("")) {
            return true;
        }

        if (!etDistrict.getText().toString().trim().equals("")) {
            return true;
        }

        if (spCountry.getSelectedItemPosition() > 0) {
            return true;
        }

        if (spinner_meetingday_anyclub.getSelectedItemPosition() > 0) {
            return true;
        }


        return false;
    }

    private void actionbarfunction() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_backbutton = (ImageView) findViewById(R.id.iv_backbutton);
        tv_title.setText("Find a Club");
    }

    // method created to show popup when lat long is 0.0,0.0
    public void popupUnableToGetLocation() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle("ROW");

        //Setting Dialog Message
        alertDialog.setMessage("Unable To Get Your Current Location.Please Try Again.");

        //On Pressing Setting button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                findAClub(tv_title);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

//            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                        GPSTracker.LOCATION_PERMISSION_REQUEST_CODE);
//            } else {
//                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//            }

            if (checkAndRequestPermissions()) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable GPS?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    String mLastUpdateTime = "";

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            Utils.log("At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    GPSTracker.LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


    private boolean checkAndRequestPermissions() {
        int permissionCOARSE = ContextCompat.checkSelfPermission(this,
               android.Manifest.permission.ACCESS_COARSE_LOCATION);


        int permissionFINE = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCOARSE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionFINE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), GPSTracker.LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }
}
