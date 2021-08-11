package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.myapplication.Database.interfaceDb;
import com.example.myapplication.Models.favHero;

import java.util.List;

public class FavHeroDisplay extends AppCompatActivity {

    private CreateDb createDb;
    private interfaceDb dao;
    List<favHero> favHeroList;
    RecyclerView recyclerView;
    Context context;
    AdapterFavHero adapterFavHero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_fav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext();

        adapterFavHero = new AdapterFavHero(R.layout.fav_hero, this);
        recyclerView.setAdapter(adapterFavHero);


        favDisplay();
    }

    public void favDisplay()
    {
        //favDoge myDataList=new favDoge(imageId, imageUrl);
        createDb = CreateDb.getInstance(context);
        dao = createDb.Dao();
        favHeroList =  dao.getFavHero();
        adapterFavHero.addAll(favHeroList);
        SearchView searchView = (SearchView) findViewById(R.id.searchFavView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                adapterFavHero.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterFavHero.getFilter().filter(newText);
                return false;
            }

        });
    }

}