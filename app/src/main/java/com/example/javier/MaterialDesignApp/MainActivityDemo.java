package com.example.javier.MaterialDesignApp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.example.javier.MaterialDesignApp.Fragments.FragmentDevelop;


public class MainActivityDemo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindemo);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        FragmentDevelop fragmentFilms = new FragmentDevelop();
        ft.replace(R.id.container, fragmentFilms);
        ft.commit();
    }






}
