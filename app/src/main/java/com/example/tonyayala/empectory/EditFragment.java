package com.example.tonyayala.empectory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;

public class EditFragment extends Fragment {

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    EditText tvTittle;
    EditText tvDescription;
    EditText tvPhone;
    EditText tvAddress;
    private Uri pickedImgUri = null;
    ImageView editPost;
    Button btnupdate;
    String pk;
    String i;
    String t;
    String d;
    String p;
    String a;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_edit, container, false );

        editPost = view.findViewById(R.id.edit_post_image);
        tvTittle = view.findViewById(R.id.title);
        tvDescription =  view.findViewById(R.id.description);
        tvPhone =  view.findViewById(R.id.phone);
        tvAddress   =  view.findViewById(R.id.address);
        btnupdate = view.findViewById(R.id.btn_update);

        Intent intent = getActivity().getIntent();
        Bundle tittle = intent.getExtras();
        pk = (String) tittle.get("postkey");

        i = (String) tittle.get("image");
        t =(String) tittle.get("tittle");
        d = (String) tittle.get("description");
        p = (String) tittle.get("phone");
        a = (String) tittle.get("address");

        Glide.with(this).load(i).into(editPost);
        tvTittle.setText(t);
        tvDescription.setText(d);
        tvPhone.setText(p);
        tvAddress.setText(a);

        setupPopupImageClick();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tvTittle.getText().toString().isEmpty()
                        &&!tvAddress.getText().toString().isEmpty()
                        &&!tvPhone.getText().toString().isEmpty()
                        &&!tvDescription.getText().toString().isEmpty()){
                    if(pickedImgUri != null){
                        updatePost();
                    }else{
                        updatePostWithoutPicture();
                    }
                }else{
                    showMessage("Todos los campos son requerdos");
                }

            }
        });

        return view;
    }

    private void setupPopupImageClick() {

        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermission();

            }
        });

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            editPost.setImageURI(pickedImgUri);

        }
    }

    private void updatePost(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("post_img");
        final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        String at = tvTittle.getText().toString();
                        String ad = tvDescription.getText().toString();
                        String ap = tvPhone.getText().toString();
                        String aa = tvAddress.getText().toString();


                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Posts");
                        databaseReference.child(pk).child("picture").setValue(imageDownloadLink)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showMessage("Los datos se actualizaron correctamente");
                                        startActivity(new Intent(getActivity(), PrincipalActivity.class));
                                    }
                                });
                        databaseReference.child(pk).child("tittle").setValue(at);
                        databaseReference.child(pk).child("description").setValue(ad);
                        databaseReference.child(pk).child("phone").setValue(ap);
                        databaseReference.child(pk).child("address").setValue(aa);
                    }
                });
            }
        });
    }

    private void updatePostWithoutPicture(){
        String at = tvTittle.getText().toString();
        String ad = tvDescription.getText().toString();
        String ap = tvPhone.getText().toString();
        String aa = tvAddress.getText().toString();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Posts");

        databaseReference.child(pk).child("tittle").setValue(at);
        databaseReference.child(pk).child("description").setValue(ad);
        databaseReference.child(pk).child("phone").setValue(ap);
        databaseReference.child(pk).child("address").setValue(aa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Los datos se actualizaron correctamente");
                        startActivity(new Intent(getContext(), PrincipalActivity.class));
                    }
                });

    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
