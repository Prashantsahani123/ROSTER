package com.NEWROW.row.Inteface;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * Created by USER on 24-03-2016.
 * Interface being implemented to check the Retrofit API call
 */
public interface DashboardAPI {

     /*Retrofit get annotation with our URL
       And our method that will return us the list ob Book
    */
    //@GET("/Group/GetAllCountriesAndCategories")

    @POST("/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    public void getGroups(@Body TypedInput jsonObject, Callback<Response> response);
}
