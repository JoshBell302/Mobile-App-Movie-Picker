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

public class LanguageData implements Serializable {
    private static final String TAG = LanguageData.class.getSimpleName();
    private String iso;
    private String english_name;
    private String name;

    public LanguageData(String i, String en, String n){
        this.iso = i;
        this.english_name = en;
        this.name = n;
        Log.d(TAG, "Building language: " + i + en + n);
    }

    public String getIso() {
        return iso;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<LanguageData> {
        @Override
        public LanguageData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject listArr = json.getAsJsonObject();

            return new LanguageData(
                    listArr.getAsJsonPrimitive("iso_639_1").getAsString(),
                    listArr.getAsJsonPrimitive("english_name").getAsString(),
                    listArr.getAsJsonPrimitive("name").getAsString()
            );
        }
    }

}
