package com.example.javier.MaterialDesignApp.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Envy on 13/03/2015.
 */
public class ListVideoModel implements Serializable {

    private String id;
    private String id_film;
    private String category;
    private String title_vn;
    private String title_en;
    private String images;
    private  String country;
    private String director;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    private String content;
    private String info;
    private String actor;
    private ArrayList<VideosModel> server;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_film() {
        return id_film;
    }

    public void setId_film(String id_film) {
        this.id_film = id_film;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle_vn() {
        return title_vn;
    }

    public void setTitle_vn(String title_vn) {
        this.title_vn = title_vn;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ArrayList<VideosModel> getServer() {
        return server;
    }

    public void setServer(ArrayList<VideosModel> server) {
        this.server = server;
    }


}
