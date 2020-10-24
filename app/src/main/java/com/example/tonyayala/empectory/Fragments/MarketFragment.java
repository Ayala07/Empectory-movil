package com.example.tonyayala.empectory.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyayala.empectory.Adapters.MarketAdapter;
import com.example.tonyayala.empectory.Models.MarketPost;
import com.example.tonyayala.empectory.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {

    private StaggeredGridLayoutManager manager;
    RecyclerView marketRecycleview;
    MarketAdapter marketAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<MarketPost> marketPostList;
    String pk;
    ImageView imgMarketEmpty;
    TextView tvMarketEmpty;

    public MarketFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate( R.layout.fragment_market, container, false );

        Intent intent = getActivity().getIntent();
        Bundle tittle = intent.getExtras();
        pk = (String) tittle.get("postkey");

        imgMarketEmpty = fragmentView.findViewById( R.id.imgMarket_empty );
        tvMarketEmpty = fragmentView.findViewById( R.id.txtMarket_Empty );

        marketRecycleview = fragmentView.findViewById( R.id.market_RV );

        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        marketRecycleview.setLayoutManager( manager );

        marketRecycleview.setHasFixedSize( true );
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference( "Market" ).child( pk );

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    imgMarketEmpty.setVisibility( View.GONE );
                    tvMarketEmpty.setVisibility( View.GONE );

                    marketPostList = new ArrayList<>();
                    for (DataSnapshot postnap : dataSnapshot.getChildren()) {
                        MarketPost marketPost = postnap.getValue( MarketPost.class );
                        marketPostList.add( marketPost );
                    }
                    manager.setReverseLayout( false );
                    marketAdapter = new MarketAdapter( getActivity(), marketPostList );
                    marketRecycleview.setAdapter( marketAdapter );

                }else{
                    imgMarketEmpty.bringToFront();

                    imgMarketEmpty.setVisibility( View.VISIBLE );
                    tvMarketEmpty.setVisibility( View.VISIBLE );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( getActivity(), "Ha ocurrido un error inesperado: " + databaseError, Toast.LENGTH_SHORT ).show();
            }
        } );

        return fragmentView;
    }
}
