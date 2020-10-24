package com.example.tonyayala.empectory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.tonyayala.empectory.RegisterActivity.PReqCode;
import static com.example.tonyayala.empectory.RegisterActivity.REQUESCODE;

public class EditProduct extends AppCompatActivity {

    EditText tvTittle;
    EditText tvPrice;
    EditText tvExists;
    EditText tvDescription;
    ImageView imgProduct;
    ImageView back;
    Button update;
    private Uri pickedImgUri = null;

    Toolbar toolbar;


    String postKey;
    String productKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_product );

        Intent intent = getIntent();
        Bundle ti = intent.getExtras();

        String tittle = (String) ti.get( "tittle" );
        String price = (String) ti.get( "price" );
        String productImage = (String) ti.get( "productImage" );
        String exist = (String) ti.get( "exist" );
        productKey = (String) ti.get( "productKey" );
        String description = (String) ti.get( "description" );
        postKey = (String) ti.get( "postKey" );


        tvTittle = findViewById( R.id.edtTittle_product );
        tvPrice = findViewById( R.id.edtPrice_product );
        tvExists = findViewById( R.id.edtExists_product );
        tvDescription = findViewById( R.id.edtDescription_product );
        imgProduct = findViewById( R.id.edit_product_image );
        update = findViewById( R.id.btnUpdateProduct );
        back = findViewById( R.id.btn_product_edit_back );



        tvTittle.setText( tittle );
        tvPrice.setText( price );
        tvExists.setText( exist );
        tvDescription.setText( description );
        Glide.with(this).load( productImage ).into( imgProduct );

        setupPopupImageClick();



        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvTittle.getText().toString().isEmpty()
                        && !tvPrice.getText().toString().isEmpty()
                        && !tvExists.getText().toString().isEmpty()
                        && !tvDescription.getText().toString().isEmpty()) {
                    if(pickedImgUri != null){
                        updateProduct();
                    }else{
                        updateProductWihtOutPhoto();
                    }
                }else{
                    showMessage( "Todos los campos son requeridos" );
                }
            }
        } );


        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( EditProduct.this, Market.class );
                intent.putExtra( "postkey" , postKey);
                startActivity( intent );
                finish();
            }
        } );

    }

    private void updateProductWihtOutPhoto() {
        String ti = tvTittle.getText().toString();
        String pr = tvPrice.getText().toString();
        String ex = tvExists.getText().toString();
        String de = tvDescription.getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Market");

        databaseReference.child( postKey ).child( productKey ).child( "tittle" ).setValue( ti );
        databaseReference.child( postKey ).child( productKey ).child( "price" ).setValue( pr );
        databaseReference.child( postKey ).child( productKey ).child( "exist" ).setValue( ex );
        databaseReference.child( postKey ).child( productKey ).child( "description" ).setValue( de )
                .addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage( "Los datos se actualilzaron correctamente" );
                Intent intent = new Intent( EditProduct.this, Market.class );
                intent.putExtra( "postkey" , postKey);
                startActivity( intent );
            }
        } );
    }

    private void updateProduct() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("market_img").child( postKey );
        final StorageReference imageFilePath = storageReference.child( pickedImgUri.getLastPathSegment() );
        imageFilePath.putFile( pickedImgUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        String ti = tvTittle.getText().toString();
                        String pr = tvPrice.getText().toString();
                        String ex = tvExists.getText().toString();
                        String de = tvDescription.getText().toString();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = database.getReference("Market");

                        databaseReference.child( postKey ).child( productKey ).child( "img" ).setValue(imageDownloadLink)
                                .addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage( "Los datos se actualilzaron correctamente" );
                                Intent intent = new Intent( EditProduct.this, Market.class );
                                intent.putExtra( "postkey" , postKey);
                                startActivity( intent );
                            }
                        } );

                        databaseReference.child( postKey ).child( productKey ).child( "tittle" ).setValue( ti );
                        databaseReference.child( postKey ).child( productKey ).child( "price" ).setValue( pr );
                        databaseReference.child( postKey ).child( productKey ).child( "exist" ).setValue( ex );
                        databaseReference.child( postKey ).child( productKey ).child( "description" ).setValue( de );

                    }
                } );

            }
        } );
    }

    private void setupPopupImageClick() {
        imgProduct.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        } );
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Por favor acepte los permisos para continuar",
                        Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(this,
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            imgProduct.setImageURI(pickedImgUri);

        }
    }

    private void showMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }
}
