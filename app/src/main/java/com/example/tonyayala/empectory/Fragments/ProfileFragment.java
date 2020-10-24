package com.example.tonyayala.empectory.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tonyayala.empectory.Adapters.PostAdapter;
import com.example.tonyayala.empectory.Models.Post;
import com.example.tonyayala.empectory.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private String sort = "old";
    private OnFragmentInteractionListener mListener;
    LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
    RecyclerView postRecycleview;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;




    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sort = getArguments().getString("sort");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate( R.layout.fragment_profile2, container, false );
        postRecycleview = fragmentView.findViewById( R.id.postRV );
        postRecycleview.setLayoutManager( linearLayout );
        postRecycleview.setHasFixedSize( true );
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference( "Posts" );
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        switch (sort) {

            case "bests":
                sortBests();
                break;
            case "new":
                sortNew();
                break;
            case "old":
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList = new ArrayList<>();
                        for (DataSnapshot postnap : dataSnapshot.getChildren()) {
                            Post post = postnap.getValue( Post.class );
                            postList.add( post );
                        }
                        linearLayout.setReverseLayout( false );
                        postAdapter = new PostAdapter( getActivity(), postList );
                        postRecycleview.setAdapter( postAdapter );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage( "Ha ocurrido un error en la base de datos: " + databaseError );
                    }
                } );
                break;
            default:
                showMessage( "Ha ocurrido un error en la base de datos" );
                break;
        }

    }

    private void sortNew() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                for (DataSnapshot postnap: dataSnapshot.getChildren()){
                    Post post = postnap.getValue(Post.class);
                    postList.add(post);
                }
                linearLayout.setReverseLayout(true);
                postAdapter = new PostAdapter(getActivity(),postList);
                postRecycleview.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Ha ocurrido un error en la base de datos: " + databaseError );
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void sortBests(){

        databaseReference.orderByChild("like").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                for (DataSnapshot postnap: dataSnapshot.getChildren()){
                    Post post = postnap.getValue(Post.class);
                    postList.add(post);
                }
                linearLayout.setReverseLayout(true);
                postAdapter = new PostAdapter(getActivity(),postList);
                postRecycleview.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Ha ocurrido un error en la base de datos: " + databaseError );
            }
        });
    }


    private void showMessage(String message) {
        Toast.makeText( getActivity(), message, Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
