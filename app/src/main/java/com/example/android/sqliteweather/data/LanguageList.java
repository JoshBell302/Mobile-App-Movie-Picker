package com.example.android.sqliteweather.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LanguageList implements Serializable {
    private ArrayList<LanguageData> languages;

    public LanguageList() {
        this.languages = null;
    }

    public ArrayList<LanguageData> getLanguagesList() {
        return languages;
    }



}
