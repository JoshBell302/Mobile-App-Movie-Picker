package com.example.android.sqliteweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.sqliteweather.data.FiveDayForecast;
import com.example.android.sqliteweather.data.FiveDayForecastRepository;
import com.example.android.sqliteweather.data.GenreList;
import com.example.android.sqliteweather.data.LanguageData;
import com.example.android.sqliteweather.data.LanguageList;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.MovieList;
import com.example.android.sqliteweather.data.MovieRepository;

import java.util.ArrayList;

public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private LiveData<ArrayList<LanguageData>> languageList;
    private LiveData<GenreList> genreList;
    private LiveData<MovieList> movieList;
    private LiveData<LoadingStatus> loadingStatus;

    public MovieViewModel() {
        this.repository = new MovieRepository();
        genreList = repository.getGenres();
        languageList = repository.getLanguage();
        movieList = repository.getMovieList();
        loadingStatus = repository.getLoadingStatus();
    }

    public LiveData<GenreList> getGenres() {
        return this.genreList;
    }

    public LiveData<MovieList> getMovieList() { return movieList; }

    public LiveData<ArrayList<LanguageData>> getLanguageList() {
        return this.languageList;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return this.loadingStatus;
    }

    public MovieData getMovie(int i){
        return movieList.getValue().getMovieList().get(i);
    }

    public void loadMovies(int mode, String apiKey, String language, String sortBy, String withGenres, String page) {
        this.repository.loadMovieDatabase(mode, apiKey, language, sortBy, withGenres, page);
    }
}
