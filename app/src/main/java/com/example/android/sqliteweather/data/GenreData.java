package com.example.android.sqliteweather.data;

import android.util.Log;

import com.example.android.sqliteweather.MainActivity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.Serializable;
import java.lang.reflect.Type;

public class GenreData implements Serializable {
    private static final String TAG = GenreData.class.getSimpleName();
    private int id;
    private String name;

    public GenreData(int i, String n){
        //Log.d(TAG, "Building genre: "+ i + n);
        this.id = i;
        this.name = n;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<GenreData> {
        @Override
        public GenreData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject listObj = json.getAsJsonObject();

            return new GenreData(
                    listObj.getAsJsonPrimitive("id").getAsInt(),
                    listObj.getAsJsonPrimitive("name").getAsString()
            );
        }
    }
}

