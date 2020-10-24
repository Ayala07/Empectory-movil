package com.example.tonyayala.empectory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.tonyayala.empectory.Adapters.TabViewPagerAdapter;

public class EditPostActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_post );

        tabLayout = findViewById( R.id.editTabLayout );
        viewPager = findViewById( R.id.editViewPage );

        tabLayout.setupWithViewPager( viewPager );
        
        setUpViewPager(viewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter( getSupportFragmentManager() );
        tabViewPagerAdapter.addFragment( new EditFragment(), "Editar" );
        tabViewPagerAdapter.addFragment( new HistoryFragment(), "Historia" );
        viewPager.setAdapter( tabViewPagerAdapter );
    }
}