package com.example.android.sqliteweather.data;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private MutableLiveData<GenreList> genreList;
    private MutableLiveData<ArrayList<LanguageData>> languageList;
    private MutableLiveData<MovieList> movieList;

    private String currentMovieName;

    private MutableLiveData<LoadingStatus> loadingStatus;

    private OpenMovieService openMovieService;

    public MovieRepository() {
        this.genreList = new MutableLiveData<>();
        this.genreList.setValue(null);

        this.languageList = new MutableLiveData<>();
        this.languageList.setValue(null);

        this.movieList = new MutableLiveData<>();
        this.movieList.setValue(null);

        this.loadingStatus = new MutableLiveData<>();
        this.loadingStatus.setValue(LoadingStatus.SUCCESS);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LanguageData.class, new LanguageData.JsonDeserializer())
                .registerTypeAdapter(GenreList.class, new GenreList.JsonDeserializer())
                .registerTypeAdapter(MovieList.class, new MovieList.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.openMovieService = retrofit.create(OpenMovieService.class);
    }

    public LiveData<GenreList> getGenres() {
        return this.genreList;
    }

    public LiveData<ArrayList<LanguageData>> getLanguage() {
        return this.languageList;
    }

    public MutableLiveData<MovieList> getMovieList() { return movieList; }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public void loadMovieDatabase(int mode, String apiKey, String language, String sortBy, String withGenres, String page) {
        if (/*shouldFetchMovies()*/true) {
            Log.d(TAG, "Going into mode: " + mode);

            this.loadingStatus.setValue(LoadingStatus.LOADING);

            if(mode == 1){
                this.genreList.setValue(null);
                Call<GenreList> req = this.openMovieService.fetchGenres(apiKey);
                req.enqueue(new Callback<GenreList>() {
                    @Override
                    public void onResponse(Call<GenreList> call, Response<GenreList> response) {
                        if (response.code() == 200) {
                            Log.d(TAG, "Valid query: " + call.request().url());
                            genreList.setValue(response.body());
                            loadingStatus.setValue(LoadingStatus.SUCCESS);
                            Log.d(TAG, "Loading genres successful");
                            Log.d(TAG, "Length of Genres: " + genreList.getValue().getGenresList().size());
                        } else {
                            loadingStatus.setValue(LoadingStatus.ERROR);
                            Log.d(TAG, "unsuccessful API request: " + call.request().url());
                            Log.d(TAG, "  -- response status code: " + response.code());
                            Log.d(TAG, "  -- response: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreList> call, Throwable t) {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        t.printStackTrace();
                    }
                });
            }else if(mode == 2){
                this.languageList.setValue(null);
                Call<ArrayList<LanguageData>> req = this.openMovieService.fetchLanguages(apiKey);
                req.enqueue(new Callback<ArrayList<LanguageData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<LanguageData>> call, Response<ArrayList<LanguageData>> response) {
                        if (response.code() == 200) {
                            languageList.setValue(response.body());
                            loadingStatus.setValue(LoadingStatus.SUCCESS);
                            Log.d(TAG, "Loading languages successful");
                            Log.d(TAG, "Length of languages: " + languageList.getValue().size());
                        } else {
                            loadingStatus.setValue(LoadingStatus.ERROR);
                            Log.d(TAG, "unsuccessful API request: " + call.request().url());
                            Log.d(TAG, "  -- response status code: " + response.code());
                            Log.d(TAG, "  -- response: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<LanguageData>> call, Throwable t) {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        t.printStackTrace();
                    }
                });
            }else if(mode == 0){
                this.movieList.setValue(null);
                Call<MovieList> req = this.openMovieService.fetchMovies(apiKey, language, sortBy, withGenres, page);
                req.enqueue(new Callback<MovieList>() {
                    @Override
                    public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                        if (response.code() == 200) {
                            Log.d(TAG, "Valid query: " + call.request().url());
                            movieList.setValue(response.body());
                            Log.d(TAG, "Valid movie: " + movieList.getValue().getMovieList().size());
                            loadingStatus.setValue(LoadingStatus.SUCCESS);
                        } else {
                            loadingStatus.setValue(LoadingStatus.ERROR);
                            Log.d(TAG, "unsuccessful API request: " + call.request().url());
                            Log.d(TAG, "  -- response status code: " + response.code());
                            Log.d(TAG, "  -- response: " + response.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieList> call, Throwable t) {
                        loadingStatus.setValue(LoadingStatus.ERROR);
                        Log.d(TAG, "unsuccessful API request: " + call.request().url());
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Log.d(TAG, "using cached movie: DEFAULT");
        }
    }
    /*
    private boolean shouldFetchForecast(String location, String units) {
        /*
         * Fetch forecast if there isn't currently one stored.
    }
    */
}
