package com.example.tonyayala.empectory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tonyayala.empectory.Models.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String pk;
    String getHistory = "";

    EditText historyTV;
    Button historyBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_history, container, false );

        historyTV = view.findViewById( R.id.edt_HistoryFragment );
        historyBtn = view.findViewById( R.id.btn_HistoryFragment );

        Intent intent = getActivity().getIntent();
        Bundle tittle = intent.getExtras();
        pk = (String) tittle.get("postkey");

        getHistory();

        historyBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!historyTV.getText().toString().isEmpty()) {
                    postHistory();
                }else{
                    showMessage( "El campo no puede quedar vacio" );
                }
            }
        } );

        return view;
    }

    private void getHistory() {

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Historia").child( pk );

        databaseReference.child( "history" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    getHistory = dataSnapshot.getValue(String.class);
                    historyTV.setText( getHistory );
                }else{
                    showMessage( "No hay datos existentes" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Ha ocurrido un error inesperado" + databaseError);
            }
        } );

    }

    private void postHistory() {
        String history = historyTV.getText().toString();

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Historia").child( pk );

        History history1 = new History( history );

        databaseReference.setValue( history1 ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    showMessage( "Se añadió correctamente" );
                }else{
                    showMessage( "Ocurrió un error al añadirlo, intente nuevamente" );
                }
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage( "Ocurrió un error inseperado, vuelvelo a intentar más tarde" + e );
            }
        } );



    }

    private void showMessage(String message) {
        Toast.makeText( getActivity(), message, Toast.LENGTH_LONG ).show();
    }

}
