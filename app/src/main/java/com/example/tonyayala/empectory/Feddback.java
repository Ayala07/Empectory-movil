package com.example.tonyayala.empectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.tonyayala.empectory.RegisterActivity.PReqCode;
import static com.example.tonyayala.empectory.RegisterActivity.REQUESCODE;

public class Feddback extends AppCompatActivity {

    EditText feedReport;
    Button feedButton;
    ImageView feedImage;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feddback );

        feedReport = findViewById( R.id.feedBack_Description );
        feedImage = findViewById( R.id.feedBack_image );
        feedButton = findViewById( R.id.feedBack_button );

        feedImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica la version de android utilizando (SDK 22 = Android Lollipop)
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }
            }
        } );

        feedButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        } );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            feedImage.setImageURI(pickedImgUri);

        }
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(Feddback.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Feddback.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Por favor acepte los permisos para continuar", Toast.LENGTH_SHORT).show();
            }
            else{
                    ActivityCompat.requestPermissions(Feddback.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }

    private void sendEmail() {
        // Reemplazamos el email por algun otro real
        String[] to = { "empectory@gmail.com" };
        String[] cc = { "" };
        enviar(to, cc, "Reporte de problema",
                feedReport.getText().toString());
    }

    @SuppressLint("IntentReset")
    private void enviar(String[] to, String[] cc,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData( Uri.parse("mailto:"));
        if(pickedImgUri != null){
            emailIntent.putExtra( Intent.EXTRA_STREAM, pickedImgUri );
        }else{
            showMessage("No hay foto");
        }

        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Inofrmar un error"));
    }

    private void showMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }

}
