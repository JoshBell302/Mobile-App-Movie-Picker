package com.example.android.sqliteweather.data;

import android.util.Log;

import com.example.android.sqliteweather.MainActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GenreList implements Serializable {
    private static final String TAG = GenreList.class.getSimpleName();
    private ArrayList<GenreData> genreList;

    public GenreList(ArrayList<GenreData> gda) {
        this.genreList = gda;
    }

    public ArrayList<GenreData> getGenresList() {
        return genreList;
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<GenreList> {
        @Override
        public GenreList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ArrayList<GenreData> genrelist = new ArrayList<>();
            JsonObject listObj = json.getAsJsonObject();
            JsonArray genreArr = listObj.getAsJsonArray("genres");
            //Log.d(TAG, "MAKES IT IN HERE ");
            for (int i = 0; i < genreArr.size(); i++) {
                JsonObject genre = genreArr.get(i).getAsJsonObject();
                genrelist.add(new GenreData(
                        genre.getAsJsonPrimitive("id").getAsInt(),
                        genre.getAsJsonPrimitive("name").getAsString()
                ));
            }
            return new GenreList(genrelist);
        }
    }
}
