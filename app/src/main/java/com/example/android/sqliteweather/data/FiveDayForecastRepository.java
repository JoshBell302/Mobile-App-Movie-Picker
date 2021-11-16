package com.example.android.sqliteweather.data;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiveDayForecastRepository {
    private static final String TAG = FiveDayForecastRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private MutableLiveData<FiveDayForecast> fiveDayForecast;
    private MutableLiveData<LoadingStatus> loadingStatus;

    private String currentLocation;
    private String currentUnits;

    private OpenWeatherService openWeatherService;

    public FiveDayForecastRepository() {
        this.fiveDayForecast = new MutableLiveData<>();
        this.fiveDayForecast.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ForecastData.class, new ForecastData.JsonDeserializer())
                .registerTypeAdapter(ForecastCity.class, new ForecastCity.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.openWeatherService = retrofit.create(OpenWeatherService.class);
    }

    public LiveData<FiveDayForecast> getFiveDayForecast() {
        return this.fiveDayForecast;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadForecast(String location, String units, String apiKey) {
        if (shouldFetchForecast(location, units)) {
            Log.d(TAG, "fetching new forecast data for location: " + location + ", units: " + units);
            this.currentLocation = location;
            this.currentUnits = units;
            this.fiveDayForecast.setValue(null);
            this.loadingStatus.setValue(LoadingStatus.LOADING);
            Call<FiveDayForecast> req = this.openWeatherService.fetchForecast(location, units, apiKey);
            req.enqueue(new Callback<FiveDayForecast>() {
                @Override
                public void onResponse(Call<FiveDayForecast> call, Response<FiveDayForecast> response) {
                    if (response.code() == 200) {
                        fiveDayForecast.setValue(response.body());
                        loadingStatus.setValue(LoadingStatus.SUCCESS);
                    } else {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        Log.d(TAG, "  -- response status code: " + response.code());
                        Log.d(TAG, "  -- response: " + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<FiveDayForecast> call, Throwable t) {
                    loadingStatus.setValue(LoadingStatus.ERROR);
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    t.printStackTrace();
                }
            });
        } else {
            Log.d(TAG, "using cached forecast data for location: " + location + ", units: " + units);
        }
    }

    private boolean shouldFetchForecast(String location, String units) {
        /*
         * Fetch forecast if there isn't currently one stored.
         */
        FiveDayForecast currentForecast = this.fiveDayForecast.getValue();
        if (currentForecast == null) {
            return true;
        }

        /*
         * Fetch forecast if there was an error fetching the last one.
         */
        if (this.loadingStatus.getValue() == LoadingStatus.ERROR) {
            return true;
        }

        /*
         * Fetch forecast if either location or units have changed.
         */
        if (!TextUtils.equals(location, this.currentLocation) || !TextUtils.equals(units, this.currentUnits)) {
            return true;
        }

        /*
         * Fetch forecast if the earliest of the current forecast data is timestamped before "now".
         */
        if (currentForecast.getForecastDataList() != null && currentForecast.getForecastDataList().size() > 0) {
            ForecastData firstForecastData = currentForecast.getForecastDataList().get(0);
            if (firstForecastData.getEpoch() * 1000L < System.currentTimeMillis()) {
                return true;
            }
        }

        /*
         * Otherwise, don't fetch the forecast.
         */
        return false;
    }
}
