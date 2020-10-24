package com.example.tonyayala.empectory.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonyayala.empectory.Models.Post;
import com.example.tonyayala.empectory.PrincipalActivity;
import com.example.tonyayala.empectory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    private Uri pickedImgUri;
    ImageView imgProfile;
    EditText tvName;
    Button update;

    private ProgressDialog progressDialog;

    String name;
    String email;
    Uri photo;

    String p;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public EditProfileFragment() {
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate( R.layout.activity_edit_profile, container, false );

        imgProfile = fragmentView.findViewById( R.id.profile_photo );
        tvName = fragmentView.findViewById( R.id.edit_profile_name );

        update = fragmentView.findViewById( R.id.btnEditProfile );

        setupPopupImageClick();

        progressDialog = new ProgressDialog(getContext());


        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();

            Glide.with( this ).load( user.getPhotoUrl() ).into( imgProfile );
            tvName.setText( name );

        } else if(user.getPhotoUrl() == null) {
            Glide.with( this ).load( R.drawable.userphotot ).into( imgProfile );
        }else{
            showMessage( "No hay usuario" );
        }
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tvName.getText().toString();

                FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();

                if (pickedImgUri != null) {
                    updateProfile( name, pickedImgUri, uid );
                    progressDialog.setMessage( "Actualizando información..." );
                    progressDialog.show();
                } else {
                    updateProfileWithoutPhoto(name,uid);
                    progressDialog.setMessage( "Actualizando información..." );
                    progressDialog.show();
                }
            }
        } );

        return fragmentView;

    }

    private void updateProfile(final String name, final Uri pickedImgUri, final FirebaseUser currentuser) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference( "Fotos de ususarios" );
        final StorageReference imageFilePath = storageReference.child( pickedImgUri.getLastPathSegment() );
        imageFilePath.putFile( pickedImgUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName( name )
                                .setPhotoUri( uri )
                                .build();

                        currentuser.updateProfile( profileUpdate ).addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                showMessage( "Se actualizó con extito" );
                                progressDialog.dismiss();
                                dataComplements();
                                Intent intent = new Intent( getActivity(), PrincipalActivity.class );

                                startActivity( intent );
                                getActivity().onBackPressed();

                            }
                        } );

                    }
                } );


            }
        } );
    }

    private void updateProfileWithoutPhoto(final String name, final FirebaseUser currentuser) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currentuser.updateProfile(profileUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage( "Se actualizó con extito" );
                progressDialog.dismiss();
                dataComplements();
                startActivity(new Intent(getActivity(), PrincipalActivity.class));
                getActivity().onBackPressed();
            }
        });
    }

    private void dataComplements() {
        String uid = user.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference( "usuarios" ).child( uid );
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = tvName.getText().toString();
                    databaseReference.child( "nombre" ).setValue( name );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Hubo un error con la base de datos " + databaseError );
            }
        } );
    }



    private void setupPopupImageClick() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermission();

            }
        });

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission( getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Por favor acepte los permisos para continuar",
                        Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            imgProfile.setImageURI(pickedImgUri);

        }
    }

    private void showMessage(String message) {
        Toast.makeText( getActivity(), message, Toast.LENGTH_SHORT ).show();
    }
}
