package com.example.mymapapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymapapplication.models.Company;
import com.example.mymapapplication.retrofit.RetrofitRepository;

import java.util.ArrayList;

public class CompanyViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Company>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Company>> mutableLiveNearByData = new MutableLiveData<>();
    private RetrofitRepository repository;

    public void init(double lat, double longi, boolean nearBy) {
        repository = repository.getInstance();
        if (nearBy) {
            mutableLiveNearByData = repository.getCompanyList(lat, longi, true);
        } else {
            mutableLiveData = (repository.getCompanyList(lat, longi, false));
        }

    }

    public LiveData<ArrayList<Company>> getCompanyRepository() {
        return mutableLiveData;
    }

    public LiveData<ArrayList<Company>> getNearByCompanyRepository() {
        return mutableLiveNearByData;
    }

}
