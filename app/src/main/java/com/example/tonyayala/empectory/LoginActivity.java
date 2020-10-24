package com.example.tonyayala.empectory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    //Definimos variables a utilizar
    EditText txtEmail;
    EditText txtPassword;
    ProgressDialog progressDialog;
    TextView mRecoverPassTv;
    Button login;
    CheckBox showPass;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Definimos los campos a usar del login activity
        txtEmail =  findViewById(R.id.txtEmail);
        txtPassword =  findViewById(R.id.txtPassword);
        login =  findViewById(R.id.btnLogin);
        mRecoverPassTv = findViewById(R.id.recoverPassTv);
        showPass = findViewById(R.id.showPassword);

        //Obtenemos instancia de la autentificacion de Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //Verifica si existen usuario registrados

        //Hace referencia a mostrar los campos ocultos de la contraseña al marcar el checkbox
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b){
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //Boton de ingresar
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                //Verifica que los campos de correo y contraseña no esten vacios
                if(mail.isEmpty() || password.isEmpty()){
                    showMessage("Verificar los campos");
                }
                else {
                    //Hace referencia a la funcion de ingresar
                    signIn(mail, password);
                    progressDialog.setMessage( "Iniciando sesión" );
                    progressDialog.show();
                }
            }
        });
        //Al hacer click al TextView recuperar contraseña
        mRecoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llama la ventana flotante de recuperar contraseña
                showRecoverPasswordDialog();
            }
        });
        //Comienza un progress dialog
        progressDialog = new ProgressDialog(this);

    }

    //Funcion para mostrar la ventana flotante
    private void showRecoverPasswordDialog() {

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recuperar contraseña");
        //definimos linearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        //Definimos un parametro de entrada... Este caso un EditText
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);
        //Añadimos el EdiText a la ventana flotant
        linearLayout.addView(emailEt);
        linearLayout.setPadding(15,15,15,15);
        //Añadimos el constructor un LinearLayout
        builder.setView(linearLayout);

        //Creamos boton recuperar
        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEt.getText().toString().trim();
                //Establecemos una funcion que enviara el correo de recuperacion
                beginEmail(email);
            }
        });

        //Creamos boton cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar la ventana
                dialog.dismiss();
            }
        });
        //Muestra la ventana
        builder.create().show();
    }
    //Funcion para enviar correo de recuperacion
    private void beginEmail(String email) {
        //Definimos un titulo para el progressDialog
        progressDialog.setMessage("Enviando correo");
        //Mostramos el progressDialog
        progressDialog.show();
        //Funcion de Firebase para enviar correos de recuperacion
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                //Si se envio con exito
                if(task.isSuccessful()){
                    showMessage("Correo enviado");
                    //Si algo falla
                }else{
                    showMessage("Fallido");
                }
            }
            //Problemas de conexion
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //Mensaje de error
                showMessage(e.getMessage());
            }
        });
    }

    //Funcion para acceder con correo y contraseña
    private void signIn(String mail, String password) {
        //Funcion de Firebase que verifica los campos que se han almacenado en autentificacion
        firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Si esta correcto
                if(task.isSuccessful()){
                    //Referencia a la funcion
                    updateUI();
                }
                //Si algo falla
                else{
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }
    //Funcion requerida
    private void updateUI() {
        //Hace referecia a la funcion verificar
        verificar();
    }
    //Funcion que ayuda ahorrar codigo con los Toast, se simplifica
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
    //Funcion para verificar los campos con FirebaseAuth
    void verificar(){
        //Hace referencia a autentificacion de Firebase
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //Si hay usuario registrado
        if (user != null) {
            //Si el correo no se ha verificado
            if(!user.isEmailVerified()){

            //Correo verificado
            }else{
                //Inicia la pantalla principal con los datos ya registrados
                progressDialog.dismiss();
                Intent e = new Intent( getApplicationContext(), PrincipalActivity.class );
                startActivity( e );
                finish();

            }
        }else{
            //Mensaje si el usuario no existe
            Toast.makeText(LoginActivity.this, "El usuario no se ha registrado", Toast.LENGTH_SHORT).show();
        }

    }



    //Hace referencia al boton registrar
    public void Registro(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }





}

