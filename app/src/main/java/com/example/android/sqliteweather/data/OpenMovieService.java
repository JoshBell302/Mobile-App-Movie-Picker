package com.example.android.sqliteweather.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenMovieService {
    @GET("configuration/languages")
    Call<ArrayList<LanguageData>> fetchLanguages(
            @Query("api_key") String apikey
    );

    @GET("genre/movie/list")
    Call<GenreList> fetchGenres(
            @Query("api_key") String apikey
    );

    @GET("discover/movie")
    Call<MovieList> fetchMovies(
            @Query("api_key") String apikey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("with_genres") String withGenres,
            @Query("page") String page
    );

    @GET("videos")
    Call<TrailerLink> fetchTrailer(
            @Query("api_key") String apikey
    );
}
