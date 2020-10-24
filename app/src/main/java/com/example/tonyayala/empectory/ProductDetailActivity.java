package com.example.tonyayala.empectory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imgProduct;
    TextView tvPrice;
    TextView tvExists;
    TextView tvTittle;
    TextView tvDescription;
    TextView tvToolbarText;
    ImageView back;
    String productKey;
    String postKey;
    String userID;
    String uId;
    FloatingActionButton edit;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_detail );

        postKey = getIntent().getExtras().getString( "postKey" );

        userID= getIntent().getExtras().getString( "userID" );

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        Window w = getWindow();
        w.setFlags( WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getSupportActionBar().hide();

        Toolbar toolbar = findViewById( R.id.toolbar_product );
        toolbar.bringToFront();
        tvToolbarText = findViewById( R.id.toolbar_product_text );
        tvToolbarText.bringToFront();
        back = findViewById( R.id.btn_product_detail_back );
        back.bringToFront();



        imgProduct = findViewById( R.id.product_detail_img );
        tvPrice = findViewById( R.id.product_detail_price );
        tvExists = findViewById( R.id.product_detail_exists );
        tvTittle = findViewById( R.id.product_detail_tittle );
        tvDescription = findViewById( R.id.product_detail_description );
        edit = findViewById( R.id.fab_edit_product );


        if(firebaseUser != null) {
            uId = firebaseUser.getUid();
            if (userID.equals( uId )) {
                edit.setVisibility( View.VISIBLE );
            } else {
                edit.setVisibility( View.GONE );
            }
        }

        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( ProductDetailActivity.this, Market.class );
                intent.putExtra( "postkey", postKey );
                startActivity( intent );
                finish();
            }
        } );

        getData();


    }

    private void getData() {

        final String productTittle = getIntent().getExtras().getString( "tittle" );
        tvTittle.setText( productTittle );
        tvToolbarText.setText( productTittle );

        final String productPrice = getIntent().getExtras().getString( "price" );
        tvPrice.setText("Por el costo de: $" + productPrice );

        final String productImg = getIntent().getExtras().getString( "productImage" );
        Glide.with( this ).load( productImg ).into( imgProduct );

        final String productExist = getIntent().getExtras().getString( "exist" );
        tvExists.setText("En existencias: " + productExist );

        productKey = getIntent().getExtras().getString( "productKey" );


        final String productDescription = getIntent().getExtras().getString( "description" );
        tvDescription.setText( productDescription );

        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProductDetailActivity.this, EditProduct.class );
                intent.putExtra( "tittle",productTittle );
                intent.putExtra( "price",productPrice );
                intent.putExtra( "productImage",productImg );
                intent.putExtra( "exist",productExist );
                intent.putExtra( "productKey",productKey );
                intent.putExtra( "description",productDescription );
                intent.putExtra( "postKey",postKey );

                startActivity( intent );

            }
        } );
    }
}
