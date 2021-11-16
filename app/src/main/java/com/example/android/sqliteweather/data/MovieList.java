package com.example.android.sqliteweather.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieList implements Serializable{
    private static final String TAG = MovieList.class.getSimpleName();
    private ArrayList<MovieData> results;

    public MovieList(ArrayList<MovieData> movLis){
        this.results = movLis;
    }

    public ArrayList<MovieData> getMovieList() {
        return results;
    }

    public void setResults(ArrayList<MovieData> results) {
        this.results = results;
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<MovieList> {
        @Override
        public MovieList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ArrayList<MovieData> movieList = new ArrayList<>();

            JsonObject listObj = json.getAsJsonObject();
            JsonArray resultsArr = listObj.getAsJsonArray("results");
            for (int i = 0; i < resultsArr.size(); i++) {
                ArrayList<Integer> idsSto = new ArrayList<>();
                JsonObject movieObj = resultsArr.get(i).getAsJsonObject();
                JsonArray genre_ids = movieObj.getAsJsonArray("genre_ids");
                for (int j = 0; j < genre_ids.size(); j++) {
                    idsSto.add(genre_ids.get(j).getAsInt());
                }

                Log.d(TAG, "Building movie: " + movieObj.getAsJsonPrimitive("original_title").getAsString());
                movieList.add(new MovieData(idsSto,
                        movieObj.getAsJsonPrimitive("id").getAsInt(),
                        movieObj.getAsJsonPrimitive("original_language").getAsString(),
                        movieObj.getAsJsonPrimitive("title").getAsString(),
                        movieObj.getAsJsonPrimitive("poster_path").getAsString(),
                        movieObj.getAsJsonPrimitive("overview").getAsString(),
                        movieObj.getAsJsonPrimitive("release_date").getAsString(),
                        movieObj.getAsJsonPrimitive("vote_average").getAsString()
                ));
            }
            //Log.d(TAG, "DABS: " + movieList.get(0).getGenre_ids().get(3));
            return new MovieList(movieList);
        }
    }

}
