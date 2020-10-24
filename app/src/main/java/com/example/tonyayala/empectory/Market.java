package com.example.tonyayala.empectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyayala.empectory.Fragments.MarketFragment;
import com.example.tonyayala.empectory.Models.MarketPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Market extends AppCompatActivity {
    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    ImageView popupProductImage;
    ImageView popupAddBtn;
    TextView popupTittle;
    TextView popupPrice;
    TextView popupDesctiption;
    TextView popupDetail;
    TextView popupExists;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String uID;
    Dialog popAddProduct;
    ProgressBar popupClickProgress;
    ProgressDialog progressDialog;
    FloatingActionButton flo;
    private Uri pickedImgUri = null;
    String pk;
    String key;
    String userId;


    FirebaseAuth firebaseAuth;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_market );

        Intent intent = getIntent();
        Bundle tittle = intent.getExtras();
        pk = (String) tittle.get( "postkey" );
        userId = (String) tittle.get( "userId" );

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        flo = findViewById( R.id.product_fab );

        if (currentUser != null) {
            uID = currentUser.getUid();
            if(uID.equals( userId )){
                flo.setVisibility( View.VISIBLE );
            }else{
                flo.setVisibility( View.GONE );
            }
        }

        flo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddProduct.show();

                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);

            }
        } );



        getSupportFragmentManager().beginTransaction().
                replace(R.id.market_container, new MarketFragment()).commit();


        getData();
    }


    private void getData() {
        iniPopup();
        setupPopupImageClick();
    }

    private void setupPopupImageClick() {
        popupProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermission();

            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Market.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Market.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Por favor acepte los permisos para continuar",
                        Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(Market.this,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            popupProductImage.setImageURI(pickedImgUri);

        }
    }

    private void iniPopup() {
        popAddProduct = new Dialog(this);
        popAddProduct.setContentView(R.layout.popup_add_product);
        popAddProduct.getWindow().setBackgroundDrawable(new ColorDrawable( Color.TRANSPARENT));
        popAddProduct.getWindow().setLayout( Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddProduct.getWindow().getAttributes().gravity = Gravity.TOP;

        popupProductImage = popAddProduct.findViewById(R.id.popup_product_img );
        popupTittle = popAddProduct.findViewById(R.id.popup_product_tittle );
        popupDesctiption = popAddProduct.findViewById(R.id.popup_product_description);
        popupDetail = popAddProduct.findViewById(R.id.popup_product_detail);
        popupExists = popAddProduct.findViewById(R.id.popup_product_exist);
        popupPrice = popAddProduct.findViewById(R.id.popup_product_price);


        popupAddBtn = popAddProduct.findViewById(R.id.popup_product_add );
        popupClickProgress = popAddProduct.findViewById(R.id.popup_product_progressBar );

        //Añade onclick listener

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                if (!popupTittle.getText().toString().isEmpty()
                        && !popupDesctiption.getText().toString().isEmpty()
                        && !popupDetail.getText().toString().isEmpty()
                        && !popupExists.getText().toString().isEmpty()
                        && !popupPrice.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    addPopupProduct();
                } else {
                    showMessage("Por favor verifique todos los campos y seleccione una foto");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }

            }
        });
        progressDialog = new ProgressDialog(this);

    }

    private void addPopupProduct() {
        progressDialog = new ProgressDialog(this);
        progressDialog.dismiss();
        final String userID = currentUser.getUid();
        //Crear objeto post para insertar en la BD
        //Asignamos nombre al hijo que se creara en la BD
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("market_img").child( pk );
        final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        //Creamos el objeto post
                        //Si hay foto de perfil

                        MarketPost marketPost = new MarketPost(
                                imageDownloadLink,
                                popupTittle.getText().toString(),
                                popupPrice.getText().toString(),
                                popupDesctiption.getText().toString(),
                                popupDetail.getText().toString(),
                                popupExists.getText().toString(),
                                userID
                        );

                        //Añadimos post a firebase
                        addPost( marketPost );
                        //Si no hay foto de perfil


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(e.getMessage());
                        popupClickProgress.setVisibility(View.INVISIBLE);
                        popupAddBtn.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

    }

    private void addPost(MarketPost marketPost) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Market").child( pk ).push();

        key = myRef.getKey();
        marketPost.setProductkey(key);
        marketPost.setPostKey( pk );

        //Se suben los datos a la BD

        myRef.setValue(marketPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Se añadio correctamente");
                progressDialog.dismiss();
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddProduct.dismiss();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }
}
