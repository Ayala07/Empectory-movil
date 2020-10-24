package com.example.tonyayala.empectory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tonyayala.empectory.Adapters.NewsAdapter;
import com.example.tonyayala.empectory.Models.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    RecyclerView newsRecyclerview;
    NewsAdapter newsAdapter;
    List<NewsItem> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news );
        newsRecyclerview = findViewById(R.id.news_rv);
        mData = new ArrayList<>();

        mData.add(new NewsItem("El Salvador: Impact Hub busca impulsar a emprendedores", "Cinco empresas que participaron en el programa del Accelerate2030 tendrán la oportunidad de participar en un conversatorio organizado por Impact Hub, acerca de la experiencia que tuvieron estas empresas emergentes, al ser parte de este programa, y sobre cómo su participación les abrió la oportunidad de conectar con inversionistas, desarrollar su empresa y poder establecer como estrategia de negocio a los objetivos de desarrollo sostenibles", "15 de Octubre 2019"));
        mData.add(new NewsItem("EAE Invierte 350.000 euros en emprendimientos que llegarán a América Latina", "L plataforma de inversión abierta de EAE Business School, EAE Invierte, inyectó un total de 350.000 euros a dos negocios emprendedores en fase de internacionalización, que participaron en el primer encuentro de la plataforma..", "15 de Octubre 2019"));
        mData.add(new NewsItem("Matías y Esteban, dos hermanos que emprenden y generan empleo en San Salvador", "El Master Panda un foodtruck es un negocio fuera de serie. Es un emprendimiento de los hermanos Menjivar, Matías de 13 y Esteban de 11 años.", "01 de Octubre 2019"));
        mData.add(new NewsItem("Pequeños emprendedores comparten experiencias en la crianza de cerdos","Familias agricultoras y emprendedoras del cantón Sihuatilapa, en el municipio de Teotepeque, departamento de La Libertad, compartieron experiencias productivas al visitar la granja porcina del productor Eduardo Menjívar, en lo relacionado con la crianza de cerdos en ambientes rurales y controlados.","18 de octubre 2019"));

        newsAdapter = new NewsAdapter(this,mData);
        newsRecyclerview.setAdapter(newsAdapter);
        newsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

    }
}
