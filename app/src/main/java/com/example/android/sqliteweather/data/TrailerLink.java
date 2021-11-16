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

public class TrailerLink implements Serializable {
    private String key;

    public TrailerLink(String ki){
        key = ki;
    }
    public String getKey() { return key; }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<TrailerLink> {
        @Override
        public TrailerLink deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String sto = "_A-6qcgExA4";

            JsonObject listObj = json.getAsJsonObject();
            JsonArray resultsArr = listObj.getAsJsonArray("results");
            if(resultsArr.size() != 0){
                JsonObject trailer = resultsArr.get(0).getAsJsonObject();
                sto = trailer.getAsJsonPrimitive("key").getAsString();
            }
            return new TrailerLink(sto);
        }
    }
}
