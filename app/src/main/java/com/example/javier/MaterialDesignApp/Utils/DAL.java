package com.example.javier.MaterialDesignApp.Utils;

import com.example.javier.MaterialDesignApp.Model.AFilmModel;
import com.example.javier.MaterialDesignApp.Model.PageModel;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jinhduong on 23/04/2015.
 */
public class DAL {
    public static ArrayList<AFilmModel> getFilms(JSONObject jsonObject) {
        ArrayList<AFilmModel> films = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("row");
            int rows = jsonArray.length();
            for (int i = 0; i < rows; i++) {
                films.add(new AFilmModel(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return films;
    }

    public static PageModel getPage(JSONObject jsonObject) {
        PageModel pageModel = new PageModel();
        try {
            pageModel = new PageModel(jsonObject);
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
        return pageModel;
    }
}
