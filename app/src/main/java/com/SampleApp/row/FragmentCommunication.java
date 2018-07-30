package com.SampleApp.row;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.SampleApp.row.Data.FindAClubResultData;
import com.SampleApp.row.Utils.AppController;
import com.SampleApp.row.Utils.Constant;
import com.SampleApp.row.Utils.InternetConnection;
import com.SampleApp.row.Utils.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

/**
 * Created by admin on 28-07-2017.
 */

public class FragmentCommunication extends Fragment {
    Context context;
    private View view;
    Bundle extras;
    FindAClubResultData clubData;
    TextView tvNewslettersCount, tvEventsCount;
    View rlNewslettersWrapper, rlEventsWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_communication, container, false);

        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        extras = getArguments();
        Utils.log("My Extras : " + extras);
        try {
            clubData = (FindAClubResultData) extras.getSerializable("clubData");
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Sorry. Something went wrong.", Toast.LENGTH_LONG).show();
        }
        loadCountDetails();
    }

    public void loadCountDetails() {
        if (InternetConnection.checkConnection(context)) {
            try {
                Hashtable<String, String> params = new Hashtable<>();
                params.put("grpId", clubData.getGrpID());

                try {
                    final ProgressDialog progressDialog = new ProgressDialog(context, R.style.TBProgressBar);
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();

                    JSONObject requestData = new JSONObject(new Gson().toJson(params));
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            Constant.GET_CLUB_COMMUNICATION_COUNT,
                            requestData,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.dismiss();

                                    try {
                                        getResult(response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Log.e("ROW", "♦Error : " + error);
                                    error.printStackTrace();
                                    Toast.makeText(context, "Failed to receive Data from server . Please try again.", Toast.LENGTH_LONG).show();

                                }
                            }
                    );

                    request.setRetryPolicy(new DefaultRetryPolicy(
                            Constant.VOLLEY_MAX_REQUEST_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));

                    Utils.log("Request : "+Constant.GETCLUBDETAILS+" Data : "+requestData);
                    AppController.getInstance().addToRequestQueue(context, request);


                } catch (JSONException jse) {
                    jse.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No internet conenction", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        rlEventsWrapper = view.findViewById(R.id.rlEventsWrapper);
        rlNewslettersWrapper = view.findViewById(R.id.rlNewslettersWrapper);
        tvEventsCount = (TextView) view.findViewById(R.id.tvEventsCounts);
        tvNewslettersCount = (TextView) view.findViewById(R.id.tvNewsLettersCount);

        rlEventsWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEvents();
            }
        });

        rlNewslettersWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNewsletters();
            }
        });
    }

    public void loadEvents() {
        Intent intent = new Intent(context, DTEvents.class);
        intent.putExtra("groupId", clubData.getGrpID());
        startActivity(intent);
    }

    public void loadNewsletters() {
        Intent intent = new Intent(context, DTNewsLetters.class);
        intent.putExtra("groupId", clubData.getGrpID());
        startActivity(intent);
    }

    /*
    * {
          "TBCountResult": {
            "status": "0",
            "message": "success",
            "EventCount": 1,
            "NewsletterCount": 0
          }
      }
    * */
    public void getResult(JSONObject response) {
        try {
            JSONObject result = response.getJSONObject("TBCountResult");
            String status = result.getString("status");
            if ( status.equals("0") ) {
                String eventsCount = result.getString("EventCount");
                String newslettersCounts = result.getString("NewsletterCount");
                tvEventsCount.setText(eventsCount);
                tvNewslettersCount.setText(newslettersCounts);
            } else {
                Toast.makeText(context, "Failed to load count", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
