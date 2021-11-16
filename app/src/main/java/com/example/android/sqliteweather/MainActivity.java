package com.example.android.sqliteweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sqliteweather.data.ForecastCity;
import com.example.android.sqliteweather.data.ForecastData;
import com.example.android.sqliteweather.data.GenreData;
import com.example.android.sqliteweather.data.GenreList;
import com.example.android.sqliteweather.data.LanguageData;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieList;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        GenreAdapter.OnGenreItemClickListener,
        LanguageAdapter.OnLanguageItemClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * To use your own OpenWeather API key, create a file called `gradle.properties` in your
     * GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
     * `$USER_HOME/.gradle/` in Windows), and add the following line:
     *
     *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
     *
     * The Gradle build for this project is configured to automatically grab that value and store
     * it in the field `BuildConfig.OPENWEATHER_API_KEY` that's used below.  You can read more
     * about this setup on the following pages:
     *
     *   https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code
     *
     *   https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
     *
     * Alternatively, you can just hard-code your API key below 🤷‍.  If you do hard code your API
     * key below, make sure to get rid of the following line (line 18) in build.gradle:
     *
     *   buildConfigField("String", "OPENWEATHER_API_KEY", OPENWEATHER_API_KEY)
     */
    private static final String OPENWEATHER_APPID = "37018d7ab26ab6677bb68c9d40c5942f";
    private static final String OPENMOVIE_APPID = "ed9eb9ebc0c0079eb7aae2b6a62fb801";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String GENRE = "genre";
    public static final String LANGUAGE = "language";


    private ForecastAdapter forecastAdapter;
    private FiveDayForecastViewModel fiveDayForecastViewModel;
    private MovieViewModel movieViewModel;
    private GenreAdapter genreAdapter;
    private LanguageAdapter languageAdapter;
    private ArrayList<LanguageData> langList;

    private SharedPreferences sharedPreferences;

    private SharedPreferences moviePreferences;
    private SharedPreferences.Editor editor;

    private ForecastCity forecastCity;

    private RecyclerView forecastListRV;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;

    private Random rand = new Random();

    private Toast errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);
        this.forecastListRV = findViewById(R.id.rv_forecast_list);
        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
        this.forecastListRV.setHasFixedSize(true);

        this.forecastListRV.setAdapter(this.forecastAdapter);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.moviePreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        this.editor = moviePreferences.edit();

        this.fiveDayForecastViewModel = new ViewModelProvider(this)
                .get(FiveDayForecastViewModel.class);
        this.loadForecast();

        this.movieViewModel = new ViewModelProvider(this)
                .get(MovieViewModel.class);

        if(this.moviePreferences.getString(GENRE, "xx") != "xx"
                && this.moviePreferences.getString(LANGUAGE, "xx") != "xx"){
            Log.d(TAG, "BUILDING MOVIE LIST FROM PREVIOUS PREFERENCES!");
            this.loadMovie();
        }


        this.movieViewModel.loadMovies(1, OPENMOVIE_APPID, "", "", "", "");
        //this.movieViewModel.loadMovies(2, OPENMOVIE_APPID);

        this.genreAdapter = new GenreAdapter(this);
        this.languageAdapter = new LanguageAdapter(this);

        this.forecastListRV.setAdapter(this.genreAdapter);

        langList = new ArrayList<>();
        langList.add(new LanguageData("en-US", "English", "English"));
        langList.add(new LanguageData("es", "Spanish", "Español"));
        langList.add(new LanguageData("fr", "French", "Français"));
        langList.add(new LanguageData("ja", "Japanese", "日本語"));
        langList.add(new LanguageData("de", "German", "Deutsch"));
        languageAdapter.updateLanguageData(langList);

        //this.movieViewModel.loadMovies(0, OPENMOVIE_APPID, "en", "popularity.desc", "28", "1");

        this.movieViewModel.getGenres().observe(
                this,
                new Observer<GenreList>() {
                    @Override
                    public void onChanged(GenreList genreList) {
                        if (genreList != null) {
                            genreAdapter.updateGenreData(genreList.getGenresList());
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle("Genre");
                        }
                    }
                }
        );

        this.movieViewModel.getMovieList().observe(
                this,
                new Observer<MovieList>() {
                    @Override
                    public void onChanged(MovieList movieList) {
                        if(movieList != null){
                            Log.d(TAG, "This is number of movies stored: " + movieList.getMovieList().size());
                            buildIntent();
                        }
                    }
                }
        );

        /*
         * Update UI to reflect changes in loading status.
         */
        this.fiveDayForecastViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            forecastListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );


    }

    public void buildIntent(){
        int q = rand.nextInt(20);
        Intent intent = new Intent(this, MovieViewActivity.class);
        intent.putExtra(MovieViewActivity.EXTRA_MOVIE_DATA, movieViewModel.getMovie(q));
        intent.putExtra(MovieViewActivity.EXTRA_GENRE_DATA, movieViewModel.getGenres().getValue());
        intent.putExtra(MovieViewActivity.EXTRA_MOVIE_LIST, movieViewModel.getMovieList().getValue());
        startActivity(intent);
    }

    @Override
    public void onGenreItemClick(GenreData genreData) {
        Log.d(TAG, "The saved genre before: " + this.moviePreferences.getString(GENRE, "100"));

        this.editor.putString(GENRE, String.valueOf(genreData.getId()));
        this.editor.apply();

        Log.d(TAG, "The saved genre: " + this.moviePreferences.getString(GENRE, "100"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Language");
        this.forecastListRV.setAdapter(this.languageAdapter);
    }

    @Override
    public void onLanguageItemClick(LanguageData languageData) {

        Log.d(TAG, "The saved language before: " + this.moviePreferences.getString(LANGUAGE, "xx"));

        this.editor.putString(LANGUAGE, languageData.getIso());
        this.editor.apply();

        Log.d(TAG, "The saved language: " + this.moviePreferences.getString(LANGUAGE, "xx"));
        this.loadMovie();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                viewForecastCityInMap();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
//            case R.id.action_video:
//                Intent i = new Intent(this, VideoActivity.class);
//                startActivity(i);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.forecastListRV.setAdapter(this.genreAdapter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Genre");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        this.loadMovie();
    }

    /**
     * Triggers a new forecast to be fetched based on current preference values.
     */
    private void loadForecast() {
        this.fiveDayForecastViewModel.loadForecast(
                this.sharedPreferences.getString(
                        getString(R.string.pref_location_key),
                        "Corvallis,OR,US"
                ),
                this.sharedPreferences.getString(
                        getString(R.string.pref_units_key),
                        getString(R.string.pref_units_default_value)
                ),
                OPENWEATHER_APPID
        );
    }

    //this.movieViewModel.loadMovies(0, OPENMOVIE_APPID, "en", "popularity.desc", "28", "1");
    private void loadMovie(){
        int sto = rand.nextInt(10)+1;
        String q = "" + sto;
        this.movieViewModel.loadMovies(
                0,
                OPENMOVIE_APPID,
                this.moviePreferences.getString(LANGUAGE, "en"),
                "popularity.desc",
                this.moviePreferences.getString(GENRE, "100"),
                q
        );
    }

    /**
     * This function uses an implicit intent to view the forecast city in a map.
     */
    private void viewForecastCityInMap() {
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
}
