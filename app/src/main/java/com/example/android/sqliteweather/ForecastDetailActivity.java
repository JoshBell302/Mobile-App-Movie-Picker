
package com.example.android.sqliteweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.ForecastCity;
import com.example.android.sqliteweather.data.ForecastData;
import com.example.android.sqliteweather.utils.OpenWeatherUtils;

import java.util.Calendar;

public class ForecastDetailActivity extends AppCompatActivity {
    private Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_movie_web:
                viewMovieWebsite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //Link to movie imdb
    private void viewMovieWebsite(){
        String movieID = "tt5109280";
        Uri movieWebUri =  Uri.parse("https://www.imdb.com/title/" + movieID);
        Intent intent = new Intent(Intent.ACTION_VIEW, movieWebUri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            if(this.errorToast != null){
                this.errorToast.cancel();
            }
            this.errorToast = Toast.makeText(this, "Error", Toast.LENGTH_LONG);
            this.errorToast.show();
        }
    }

    /*
    * private void viewForecastCityInMap() {
        if (this.forecastCity != null) {
            Uri forecastCityGeoUri = Uri.parse(getString(
                    R.string.geo_uri,
                    this.forecastCity.getLatitude(),
                    this.forecastCity.getLongitude(),
                    12
            ));
            Intent intent = new Intent(Intent.ACTION_VIEW, forecastCityGeoUri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                if (this.errorToast != null) {
                    this.errorToast.cancel();
                }
                this.errorToast = Toast.makeText(
                        this,
                        getString(R.string.action_map_error),
                        Toast.LENGTH_LONG
                );
                this.errorToast.show();
            }
        }
    }
    * */

    /**
     * This method uses an implicit intent to launch the Android Sharesheet to allow the user to
     * share the current forecast.
     */
//    private void shareForecastText() {
//        if (this.forecastData != null && this.forecastCity != null) {
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//            Calendar date = OpenWeatherUtils.dateFromEpochAndTZOffset(
//                    forecastData.getEpoch(),
//                    forecastCity.getTimezoneOffsetSeconds()
//            );
//            String unitsPref = sharedPreferences.getString(
//                    getString(R.string.pref_units_key),
//                    getString(R.string.pref_units_default_value)
//            );
//            String shareText = getString(
//                    R.string.share_forecast_text,
//                    getString(R.string.app_name),
//                    this.forecastCity.getName(),
//                    getString(
//                            R.string.forecast_date_time,
//                            getString(R.string.forecast_date, date),
//                            getString(R.string.forecast_time, date)
//                    ),
//                    this.forecastData.getShortDescription(),
//                    getString(
//                            R.string.forecast_temp,
//                            forecastData.getHighTemp(),
//                            /* get correct temperature unit for unit preference setting */
//                            OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//                    ),
//                    getString(
//                            R.string.forecast_temp,
//                            forecastData.getLowTemp(),
//                            /* get correct temperature unit for unit preference setting */
//                            OpenWeatherUtils.getTemperatureDisplayForUnitsPref(unitsPref, this)
//                    ),
//                    getString(R.string.forecast_pop, this.forecastData.getPop())
//            );
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
//            sendIntent.setType("text/plain");
//
//            Intent chooserIntent = Intent.createChooser(sendIntent, null);
//            startActivity(chooserIntent);
//        }
//    }

}