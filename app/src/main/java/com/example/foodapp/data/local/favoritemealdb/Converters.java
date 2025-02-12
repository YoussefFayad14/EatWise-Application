package com.example.foodapp.data.local.favoritemealdb;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromList(List<String> list) {
        return list != null ? gson.toJson(list) : null;
    }

    @TypeConverter
    public static List<String> toList(String data) {
        if (data == null) return null;
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(data, listType);
    }
}
