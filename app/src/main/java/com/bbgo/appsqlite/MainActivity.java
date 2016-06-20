package com.bbgo.appsqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "sqlAstros";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Astro lua = new Astro(0,"Lua","Perto da terra", "satélite");
        Astro sol = new Astro(0,"Sol","Centro da galáxia", "estrela");
        Astro terra = new Astro(0,"Terra", "Lar doce lar", "planeta");
        Astro marte = new Astro(0,"Marte", "ETs", "planeta");

        //Declara classe com SQL
        AstroDB db = new AstroDB(getBaseContext());

        //Salva elementos
        db.save(lua);
        db.save(sol);
        db.save(terra);
        db.save(marte);

        //Retorna todos os planetas
        List<Astro> listaPlanetas = db.findAllByTipo("planeta");
        Log.i(TAG, "#### LISTA DE PLANETAS");
        for(Astro l:listaPlanetas){
            Log.i(TAG, l.toString());
        }

        //Cria nova instância do DB. A anterior foi finalizada pela método findAllByTipo
        //db = new AstroDB(getBaseContext());
        List<Astro> listaAstros = db.findAll();
        Log.i(TAG, "#### LISTA TODOS OS ASTROS");
        for(Astro l:listaAstros){
            Log.i(TAG, l.toString());
        }

        //Cria nova isntância do DB
        //db = new AstroDB(getBaseContext());
        db.deleteAstrosByTipo("planeta");

        //Cria nova isntância do DB
        //db = new AstroDB(getBaseContext());
        List<Astro> listaAstrosSemPlanetas = db.findAll();
        Log.i(TAG, "#### LISTA DE ASTROS SEM PLANETAS");
        for(Astro l:listaAstrosSemPlanetas){
            Log.i(TAG, l.toString());
        }
    }
}
