package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.myapplication.API.APIHeroes;
import com.example.myapplication.API.interfaceHeroes;
import com.example.myapplication.Models.ModelHero;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static String apiKey = "0bee7108-b9af-414c-8a97-2f803b14ca45";
    private final static String TAG = "idot";
    public AdapterHeroes adapterHeroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapterHeroes = new AdapterHeroes(R.layout.list_hero, this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_hero);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterHeroes);

        interfaceHeroes heroService = APIHeroes.getClient().create(interfaceHeroes.class);
        Call<List<ModelHero>> call = heroService.getHero();

        call.enqueue(new Callback<List<ModelHero>>() {
            @Override
            public void onResponse(Call<List<ModelHero>> call, Response<List<ModelHero>> response) {
                List<ModelHero> heroList = response.body();
                adapterHeroes.addAll(heroList);
                Log.d(TAG, String.valueOf(heroList.size()));
            }

            @Override
            public void onFailure(Call<List<ModelHero>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Log.d(TAG, String.valueOf(t));
            }
        });

        btnClick();
    }

    public void btnClick()
    {
        ImageButton btnFav = (ImageButton) findViewById(R.id.favButton);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavHeroDisplay.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterHeroes.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterHeroes.getFilter().filter(newText);
                return false;
            }

        });
    }

}
