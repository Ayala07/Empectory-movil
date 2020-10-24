package com.example.tonyayala.empectory;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tonyayala.empectory.objetos.FirebaseReferences;
import com.example.tonyayala.empectory.objetos.prueba;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView imgUserPhoto;
    Button btnReigstrar;
    Button btnInicio;
    EditText edtNombre;
    EditText edtUsuario;
    EditText edtPass;
    EditText edtPass2;
    EditText edtEmail;
    private ProgressDialog progressDialog;
    CheckBox showPass;
    CheckBox withPlan;
    CheckBox withoutPlan;

    private FirebaseAuth firebaseAuth;

    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    int identificator = 0;
    String plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Obtencion de instancia de firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //Relacion de variables con los compotentes del layout
        btnInicio =  findViewById(R.id.btnIncio);
        btnReigstrar =  findViewById(R.id.btnRegistrar);
        edtNombre =  findViewById(R.id.edtNombre);
        edtUsuario =  findViewById(R.id.edtUsuario);
        showPass =  findViewById(R.id.showPasswordReg);
        withPlan =  findViewById(R.id.withPlan);
        withoutPlan =  findViewById(R.id.withoutPlan);
        edtPass =  findViewById(R.id.edtPassword);
        edtPass2 =  findViewById(R.id.edtPassword2);
        edtEmail =  findViewById(R.id.edtEmail);
        imgUserPhoto = findViewById(R.id.regUserPhoto);


        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        withPlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    identificator = 1;
                    withoutPlan.setChecked(false);
                    plan = "Con plan de paga";
                }
            }
        });

        withoutPlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    identificator = 0;
                    withPlan.setChecked(false);
                    plan = "Con plan gratuito";
                }
            }
        });
        //Ejecuta la barra de progreso
        progressDialog = new ProgressDialog(this);

        //Se crea funcion OnClick para el boton inciar sesion
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        //Se crea funcion OnClick para el boton registrar (final del codigo)
        btnReigstrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos el email y la contraseña desde las cajas de texto
                String name = edtNombre.getText().toString().trim();
                String user = edtUsuario.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String selectedPlan = plan;
                String password = edtPass.getText().toString().trim();
                String password2 =  edtPass2.getText().toString().trim();

                    if(name.isEmpty() || user.isEmpty() || email.isEmpty() || !password.equals(password2)){
                        showMessage("Verifica los campos por favor");
                    }

                    else{
                        CreateUserAccount(email,name,password, selectedPlan);
                    }
                }



        });

        //Se crea funcion OnClick para el ImageView del usuario
        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica la version de android utilizando (SDK 22 = Android Lollipop)
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }

            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Por favor acepte los permisos para continuar", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            imgUserPhoto.setImageURI(pickedImgUri);

        }
    }


    private void CreateUserAccount(String email, final String name, String password, final String plan) {
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            showMessage("Se ha registrado el usuario con el email: " + edtEmail.getText());

                            //Verificamos si se ha seleccionado una foto de perfil
                            if (pickedImgUri != null) {
                                updateUserInfo(name , pickedImgUri, firebaseAuth.getCurrentUser());
                            }else {
                             updateUserInfoWithoutPhoto(name, firebaseAuth.getCurrentUser(), plan);
                            }

                            //Agregar campos a la base de datos
                            complementoDatos();
                            //Envia correo electronico al email ingresdo
                            enviarEmail();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            //Si se presenta una colosion
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegisterActivity.this, "El correo ya está existente", Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        //Subiremos la url de la imagen
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Fotos de ususarios");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Se obtiene correctamente
                //Obtenemos la uri
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Se sube correctamente
                                if (task.isSuccessful()) {
                                    updateUI();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void updateUserInfoWithoutPhoto(final String name, final FirebaseUser currentUser, String plan) {

                //Obtenemos la uri

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Se sube correctamente
                                if (task.isSuccessful()) {
                                    updateUI();
                                }
                            }
                        });
                    }


    private void updateUI() {
        //
    }

    //Metodo para mostrar mensaje
    private void showMessage(String message) {
    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    void complementoDatos() {
        final String nombre = edtNombre.getText().toString().trim();
        final String usuario = edtUsuario.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();

        try {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();

            //Crea instancia de la base de datos
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Obtiene referencia de un objeto creado en la carpeta objeto (usuario en este caso)
            final DatabaseReference databaseReference = database.getReference(FirebaseReferences.USUARIO_REFERENCE);

            prueba prueba = new prueba(nombre, usuario, email, plan);
            //Creará en la base de datos una regla dentro usuarios
            databaseReference.child(userId).setValue(prueba);
        } catch (Exception e) {
            Toast.makeText(this, "Error 128: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void enviarEmail(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification();
    }


}

