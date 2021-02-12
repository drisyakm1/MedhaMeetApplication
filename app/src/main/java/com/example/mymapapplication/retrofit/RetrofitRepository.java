package com.example.mymapapplication.retrofit;

import androidx.lifecycle.MutableLiveData;

import com.example.mymapapplication.models.Company;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRepository {

    private static RetrofitRepository retrofitRepository;

    public static RetrofitRepository getInstance() {
        if (retrofitRepository == null) {
            retrofitRepository = new RetrofitRepository();
        }
        return retrofitRepository;
    }

    private ApiInterface apiInterface;

    public RetrofitRepository() {
        apiInterface = APIClient.getRetrofitInstance().create(ApiInterface.class);
    }

    MutableLiveData<ArrayList<Company>> companyData = new MutableLiveData<>();
    MutableLiveData<ArrayList<Company>> nearBycompanyData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Company>> getCompanyList(double lat, double longi, boolean nearBy) {

        apiInterface.getNearByCompanies(lat, longi).enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(Call<ArrayList<Company>> call, Response<ArrayList<Company>> response) {
                if (response.isSuccessful()) {
                    if (nearBy) {
                        nearBycompanyData.postValue(response.body());
                    } else {
                        companyData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Company>> call, Throwable t) {

            }
        });
        return companyData;
    }


}
