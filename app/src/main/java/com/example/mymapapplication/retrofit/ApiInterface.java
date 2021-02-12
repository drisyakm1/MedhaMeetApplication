package com.example.mymapapplication.retrofit;

import com.example.mymapapplication.models.Company;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("companies/getallcompanies/{latitude}/{longitude}")
    Call<ArrayList<Company>> getNearByCompanies(@Path("latitude") double latitude, @Path("longitude") double longitude);


}
