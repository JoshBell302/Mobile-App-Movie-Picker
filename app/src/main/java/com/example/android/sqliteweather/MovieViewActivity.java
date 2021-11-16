
package com.example.android.sqliteweather;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.ForecastCity;
import com.example.android.sqliteweather.data.ForecastData;
import com.example.android.sqliteweather.data.GenreData;
import com.example.android.sqliteweather.data.GenreList;
import com.example.android.sqliteweather.data.LanguageData;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.MovieList;
import com.example.android.sqliteweather.data.OpenMovieService;
import com.example.android.sqliteweather.data.TrailerLink;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpCookie;
import java.util.Random;
import java.util.ResourceBundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieViewActivity extends AppCompatActivity {
    private static final String TAG = MovieViewActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE_DATA = "MovieViewActivity.MovieData";
    public static final String EXTRA_GENRE_DATA = "MovieViewActivity.GenreList";
    public static final String EXTRA_MOVIE_LIST = "MovieViewActivity.MovieList";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String OPENMOVIE_APPID = "ed9eb9ebc0c0079eb7aae2b6a62fb801";

    private MovieList movieList = null;
    private MovieData movieData = null;
    private GenreList genreList = null;
    private MutableLiveData<TrailerLink> trailerKey;

    private Random rand;
    private Toast errorToast;
    private OpenMovieService openMovieService;
    private Gson gson;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        this.trailerKey = new MutableLiveData<>();
        rand = new Random();

        this.gson = new GsonBuilder()
                .registerTypeAdapter(TrailerLink.class, new TrailerLink.JsonDeserializer())
                .create();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_MOVIE_DATA)) {
            this.movieData = (MovieData) intent.getSerializableExtra(EXTRA_MOVIE_DATA);
            Log.d(TAG, "this poster address going in: " + movieData.getPoster_path());
            buildView();
        }

//        Call<TrailerLink> req = this.openMovieService.fetchTrailer("43a7bb8517e901d4b5af02da899af56b");


        if(intent != null && intent.hasExtra(EXTRA_GENRE_DATA)){
            this.genreList = (GenreList) intent.getSerializableExtra(EXTRA_GENRE_DATA);
        }

        if(intent != null && intent.hasExtra(EXTRA_MOVIE_LIST)){
            this.movieList = (MovieList) intent.getSerializableExtra(EXTRA_MOVIE_LIST);
        }





    }

    //This stuff holds the different genres, description, rating and so on
    public void buildDetail(){
        setContentView(R.layout.activity_movie_detail);
        TextView movieTitleTV = findViewById(R.id.tv_movie_title);
        movieTitleTV.setText(movieData.getTitle());

        TextView infoBox = findViewById(R.id.info_text);
        infoBox.setText(movieData.getOverview());

        TextView releaseDate = findViewById(R.id.info_text2);
        releaseDate.setText(movieData.getRelease());

        TextView rating = findViewById(R.id.info_text3);
        rating.setText(movieData.getRating());

        TextView genres = findViewById(R.id.info_text4);
        String sto = "";
        for (int i = 0; i < movieData.getGenre_ids().size(); i++) {
            if(i != 0){
                sto += ", ";
            }
            for (int j = 0; j < genreList.getGenresList().size(); j++) {
                if(movieData.getGenre_ids().get(i) == genreList.getGenresList().get(j).getId()){
                    sto += genreList.getGenresList().get(j).getName();
                }
            }
        }

        genres.setText(sto);

        LinearLayout linearHolder = findViewById(R.id.ll_movie_detail);
        linearHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildView();
            }
        });
    }

    //Displays the poster for the movie
    public void buildView(){
        setContentView(R.layout.activity_view_movie);
        TextView movieName = findViewById(R.id.tv_movie_name);
        movieName.setText(movieData.getTitle());
        ImageView posterIV = findViewById(R.id.iv_movie_icon);
        Glide.with(this)
                .load(this.movieData.getPoster_path())
                .into(posterIV);

        posterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDetail();
            }
        });

        ImageView xMark = findViewById(R.id.iv_unfavorite_icon);
        xMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieData = movieList.getMovieList().get(rand.nextInt(20));
                buildView();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + movieData.getId() + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.openMovieService = retrofit.create(OpenMovieService.class);

        this.trailerKey.setValue(null);

        Call<TrailerLink> req = this.openMovieService.fetchTrailer(OPENMOVIE_APPID);
        req.enqueue(new Callback<TrailerLink>() {
            @Override
            public void onResponse(Call<TrailerLink> call, Response<TrailerLink> response) {
                if(response.code() == 200){
                    Log.d(TAG, "Valid query: " + call.request().url());
                    trailerKey.setValue(response.body());
                    Log.d(TAG, "Valid Key aquired: " + trailerKey.getValue().getKey());
                }else {
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    Log.d(TAG, "  -- response status code: " + response.code());
                    Log.d(TAG, "  -- response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<TrailerLink> call, Throwable t) {
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            case R.id.action_video:
                Intent i = new Intent(this, VideoActivity.class);
                i.putExtra(VideoActivity.EXTRA_TRAILER_KEY, trailerKey.getValue());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Link to movie imdb
    private void viewMovieWebsite(){
        Uri movieWebUri =  Uri.parse("https://www.themoviedb.org/movie/" + movieData.getId());
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
}