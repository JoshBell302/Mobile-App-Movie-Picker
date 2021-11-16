package com.example.android.sqliteweather;

import com.example.android.sqliteweather.data.FiveDayForecast;
import com.example.android.sqliteweather.data.FiveDayForecastRepository;
import com.example.android.sqliteweather.data.LoadingStatus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FiveDayForecastViewModel extends ViewModel {
    private FiveDayForecastRepository repository;
    private LiveData<FiveDayForecast> fiveDayForecast;
    private LiveData<LoadingStatus> loadingStatus;

    public FiveDayForecastViewModel() {
        this.repository = new FiveDayForecastRepository();
        fiveDayForecast = repository.getFiveDayForecast();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<FiveDayForecast> getFiveDayForecast() {
        return this.fiveDayForecast;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadForecast(String location, String units, String apiKey) {
        this.repository.loadForecast(location, units, apiKey);
    }
}
