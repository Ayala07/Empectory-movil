package com.example.tonyayala.empectory.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tonyayala.empectory.Adapters.FavoriteAdapter;
import com.example.tonyayala.empectory.Models.Favorite;
import com.example.tonyayala.empectory.Models.Like;
import com.example.tonyayala.empectory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    LinearLayoutManager linearLayout = new LinearLayoutManager( getActivity() );
    RecyclerView favRecyclerView;
    FavoriteAdapter favoriteAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    List<Favorite> favoriteList;

    public FavoriteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragentView = inflater.inflate( R.layout.fragment_favorite, container, false );

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = firebaseUser.getUid();

        favRecyclerView = fragentView.findViewById( R.id.favRV );
        favRecyclerView.setLayoutManager( linearLayout );
        favRecyclerView.hasFixedSize();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("usuarios").child( userID ).child( "Favorites" );

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                  favoriteList = new ArrayList<>(  );
                  for(DataSnapshot favsnap : dataSnapshot.getChildren()){
                      Favorite favorite = favsnap.getValue(Favorite.class);
                      favoriteList.add( favorite );
                  }
                  linearLayout.setReverseLayout( false );
                  favoriteAdapter = new FavoriteAdapter( getActivity(), favoriteList );
                  favRecyclerView.setAdapter( favoriteAdapter );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        return fragentView;
    }

    private void showMessage(String message) {
        Toast.makeText( getActivity(), message, Toast.LENGTH_SHORT ).show();
    }


}
