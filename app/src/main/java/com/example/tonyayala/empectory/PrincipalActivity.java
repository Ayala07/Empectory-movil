package com.example.tonyayala.empectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonyayala.empectory.Fragments.EditProfileFragment;
import com.example.tonyayala.empectory.Fragments.FavoriteFragment;
import com.example.tonyayala.empectory.Fragments.PrincipalFragment;
import com.example.tonyayala.empectory.Fragments.ProfileFragment;
import com.example.tonyayala.empectory.Models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

/*@(#) */

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallBacks;
    Dialog popAddPost;
    ImageView popupUserImage;
    ImageView popupFaceInfo;
    ImageView popupPostImage;
    ImageView popupAddBtn;
    TextView popupTittle;
    TextView popupDescription;
    TextView popupFacebook;
    TextView popupPhone;
    TextView popupAddress;
    TextView popupEmail;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    Dialog myDialog;
    String cod;
    EditText phoneEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_principal );
        myDialog = new Dialog( this );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        toolbar.setTitle( "Últimos emprendedores" );

        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Ejecuta la barra de progreso
        progressDialog = new ProgressDialog( this );


        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();

                popupClickProgress.setVisibility( View.INVISIBLE );
                popupAddBtn.setVisibility( View.VISIBLE );

                TextView getEmail = findViewById( R.id.nav_user_email );
                TextView email = new TextView( PrincipalActivity.this );
                email.setText( getEmail.getText() );

                popupEmail.setText( email.getText() );

            }
        } );

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        getSupportFragmentManager().beginTransaction().
                replace( R.id.container, new ProfileFragment() ).commit();

        getdata();
    }

    private void faceDialog() {
        TextView txtclose;
        myDialog.setContentView( R.layout.activity_info_facebook );

        txtclose = myDialog.findViewById( R.id.close_about_TV );

        txtclose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        } );

        myDialog.show();
    }

    private void getdata() {

        iniPopup();

        setupPopupImageClick();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        try {
            if (user != null) {


                if (user.isEmailVerified()) {

                    sesionUp();

                } else {
                    sesionClose();
                }
            } else {
                sesionClose();
            }
        } catch (Exception x) {
            Toast.makeText( this, "error 150" + x.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    @SuppressLint("RestrictedApi")
    private void sesionClose() {
        //Sesion cerrada
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setVisibility( View.INVISIBLE );

        navigationView = findViewById( R.id.nav_view );
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem( R.id.nav_signout ).setVisible( false );
        navMenu.findItem( R.id.nav_edit_profile ).setVisible( false );
        navMenu.findItem( R.id.nav_fav ).setVisible( false );

        View headerView = navigationView.getHeaderView( 0 );
        TextView navUserPlan = headerView.findViewById( R.id.nav_user_plan );
        navUserPlan.setVisibility( View.INVISIBLE );

    }

    @SuppressLint("RestrictedApi")
    private void sesionUp() {
        //Sesion abierta
        Glide.with( this ).load( currentUser.getPhotoUrl() ).into( popupUserImage );

        updateNavHeader();

        navigationView = findViewById( R.id.nav_view );
        Menu navMenu = navigationView.getMenu();
        navMenu.findItem( R.id.nav_signin ).setVisible( false );
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener( mAuthListener );
    }


    private void setupPopupImageClick() {

        popupPostImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestForPermission();

            }
        } );

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission( PrincipalActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale( PrincipalActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE )) {
                Toast.makeText( this, "Por favor acepte los permisos para continuar",
                        Toast.LENGTH_SHORT ).show();

            } else {
                ActivityCompat.requestPermissions( PrincipalActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode );
            }
        } else
            openGallery();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent( Intent.ACTION_GET_CONTENT );
        galleryIntent.setType( "image/*" );
        startActivityForResult( galleryIntent, REQUESCODE );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            //El usuario selecciono foto
            //Se necesita guardar la referencia de la variable de URL

            pickedImgUri = data.getData();
            popupPostImage.setImageURI( pickedImgUri );

        }
    }


    private void iniPopup() {
        popAddPost = new Dialog( this );
        popAddPost.setContentView( R.layout.popup_add_post );
        popAddPost.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        popAddPost.getWindow().setLayout( Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT );
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //widgets

        popupUserImage = popAddPost.findViewById( R.id.popup_user );
        popupPostImage = popAddPost.findViewById( R.id.popup_post_img );
        popupTittle = popAddPost.findViewById( R.id.popup_post_tittle );
        popupDescription = popAddPost.findViewById( R.id.popup_post_price );
        popupEmail = popAddPost.findViewById( R.id.popup_post_description );

        popupPhone = popAddPost.findViewById( R.id.popup_post_phone );
        popupAddress = popAddPost.findViewById( R.id.popup_post_name );
        popupFaceInfo = popAddPost.findViewById( R.id.popup_facebook_info );
        popupFacebook = popAddPost.findViewById( R.id.popup_facebook );
        popupAddBtn = popAddPost.findViewById( R.id.popup_post_add );
        popupClickProgress = popAddPost.findViewById( R.id.popup_post_progressBar );

        //Llama datos

        popupFaceInfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceDialog();
            }
        } );


        //Añade onclick listener

        popupAddBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility( View.INVISIBLE );
                popupClickProgress.setVisibility( View.VISIBLE );

                if (!popupTittle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && !popupPhone.getText().toString().isEmpty()
                        && !popupAddress.getText().toString().isEmpty()
                        && !popupFacebook.getText().toString().isEmpty()
                        && !popupEmail.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    //Mostramos ventana con la informacion del celular
                    showInfoMessagePhone();
                } else {
                    showMessage( "Por favor verifique todos los campos y seleccione una foto" );
                    popupAddBtn.setVisibility( View.VISIBLE );
                    popupClickProgress.setVisibility( View.INVISIBLE );
                }

            }
        } );
        progressDialog = new ProgressDialog( this );
    }

    private void addPopupPost(PhoneAuthCredential credential) {
        progressDialog.dismiss();
        //Crear objeto post para insertar en la BD
        //Asignamos nombre al hijo que se creara en la BD
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child( "post_img" );
        final StorageReference imageFilePath = storageReference.child( pickedImgUri.getLastPathSegment() );
        imageFilePath.putFile( pickedImgUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageDownloadLink = uri.toString();
                        //Creamos el objeto post
                        //Si hay foto de perfil
                        if (currentUser.getPhotoUrl() != null) {
                            Post post = new Post( popupTittle.getText().toString(),
                                    popupDescription.getText().toString(),
                                    popupPhone.getText().toString(),
                                    popupEmail.getText().toString(),
                                    popupAddress.getText().toString(),
                                    popupFacebook.getText().toString(),
                                    imageDownloadLink,
                                    currentUser.getUid(),
                                    0,
                                    currentUser.getPhotoUrl().toString() );

                            //Añadimos post a firebase
                            addPost( post );
                            //Si no hay foto de perfil
                        } else {
                            Post post = new Post( popupTittle.getText().toString(),
                                    popupDescription.getText().toString(),
                                    popupPhone.getText().toString(),
                                    popupEmail.getText().toString(),
                                    popupAddress.getText().toString(),
                                    popupFacebook.getText().toString(),
                                    imageDownloadLink,
                                    currentUser.getUid(),
                                    0,
                                    null );

                            //Añadimos post a firebase
                            addPost( post );
                        }


                    }
                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage( e.getMessage() );
                        popupClickProgress.setVisibility( View.INVISIBLE );
                        popupAddBtn.setVisibility( View.VISIBLE );
                    }
                } );

            }
        } );


    }

    @SuppressLint("SetTextI18n")
    private void showInfoMessagePhone() {
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        progressDialog = new ProgressDialog( this );
        builder.setTitle( "Verificación" );
        builder.setMessage( "Se enviará un mensaje con un código al número: " + popupPhone.getText().toString() + ". Puede generar cargos extra" );
        builder.setPositiveButton( "Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showNumberPhoneConfirm();
                sendCode();

            }
        } );
        //Boton cancelar
        builder.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar la ventana
                dialog.dismiss();
                popupClickProgress.setVisibility( View.INVISIBLE );
                popupAddBtn.setVisibility( View.VISIBLE );

            }
        } );
        //Muestra la ventana
        builder.create().show();
    }

    private void sendCode() {
        String phonrNumber = popupPhone.getText().toString();
        progressDialog.setMessage( "Envíando mensaje de verificación..." );
        progressDialog.show();
        setUpVerificationCallBacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonrNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallBacks );        // OnVerificationStateChangedCallbacks
    }

    private void setUpVerificationCallBacks() {
        verificationCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                phoneVerificationId = verificationId;
                progressDialog.dismiss();
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                cod = credential.getSmsCode();

                if (cod != null) {
                    phoneEdt.setText( cod );
                    progressDialog.dismiss();
                } else {
                    showMessage( "Ocurrió un error" );
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                showMessage( "Código invalido" );
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    progressDialog.dismiss();
                    //Verificacion invalida
                    popupAddBtn.setVisibility( View.VISIBLE );
                    popupClickProgress.setVisibility( View.INVISIBLE );

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    //Cuando hace demasiados pedidos
                    showMessage( "Se ha agotado la cantidad de mensajes que puedes solicitar. Intentalo de nuevo más tarde" );
                    progressDialog.dismiss();
                    popupAddBtn.setVisibility( View.VISIBLE );
                    popupClickProgress.setVisibility( View.INVISIBLE );
                }
            }


        };

    }

    private void verifyCode(String code) {
        progressDialog.dismiss();
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential( phoneVerificationId, code );
        addPopupPost( phoneAuthCredential );
    }

    @SuppressLint("Assert")
    private void showNumberPhoneConfirm() {
        progressDialog.dismiss();
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Ingresar código de verificación" );

        //definimos linearLayout
        LinearLayout linearLayout = new LinearLayout( this );


        phoneEdt = new EditText( this );
        phoneEdt.setHint( "Código de verificación" );
        phoneEdt.setInputType( InputType.TYPE_CLASS_PHONE );


        phoneEdt.setMinEms( 16 );

        linearLayout.addView( phoneEdt );
        linearLayout.setPadding( 15, 15, 15, 15 );

        builder.setView( linearLayout );


        //Boton recuperar
        builder.setPositiveButton( "Verificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (phoneEdt.getText().toString().equals( "" )) {
                    showNumberPhoneConfirm();
                    showMessage( "El campo no puede quedar vacio" );
                } else {
                    String code = phoneEdt.getText().toString();
                    progressDialog.setMessage( "Verificando" );
                    progressDialog.show();
                    verifyCode( code );

                }
            }
        } );
        //Boton cancelar
        builder.setNegativeButton( "Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar la ventana
                dialog.dismiss();
                popupClickProgress.setVisibility( View.INVISIBLE );
                popupAddBtn.setVisibility( View.VISIBLE );
            }
        } );
        //Muestra la ventana
        builder.create().show();
    }


    private void addPost(Post post) {
        //Se crea el padre

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "Posts" ).push();

        String key = myRef.getKey();
        post.setPostKey( key );

        //Se suben los datos a la BD

        myRef.setValue( post ).addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage( "Se añadio correctamente" );
                progressDialog.dismiss();
                popupClickProgress.setVisibility( View.INVISIBLE );
                popupAddBtn.setVisibility( View.VISIBLE );
                popAddPost.dismiss();
            }
        } );

    }

    private void showMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.principal, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.shortBy) {
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle( "Inicio" );
            getSupportFragmentManager().beginTransaction().replace( R.id.container, new PrincipalFragment() ).commit();

        } else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle( "Directorio" );
            getSupportFragmentManager().beginTransaction().replace( R.id.container, new ProfileFragment() ).commit();

        } else if (id == R.id.nav_edit_profile) {
           getSupportActionBar().setTitle( "Editar perfil" );
           getSupportFragmentManager().beginTransaction().replace( R.id.container, new EditProfileFragment() ).commit();

        } else if (id == R.id.nav_fav) {
            getSupportActionBar().setTitle( "Favoritos" );
            getSupportFragmentManager().beginTransaction().replace( R.id.container, new FavoriteFragment() ).commit();

        } else if (id == R.id.nav_news) {
            Intent news = new Intent( PrincipalActivity.this, NewsActivity.class );
            startActivity( news );

        } else if (id == R.id.nav_help) {
            Intent help = new Intent( PrincipalActivity.this, help2Activity.class );
            startActivity( help );

        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent out = new Intent( getApplicationContext(), PrincipalActivity.class );
            startActivity( out );
        } else if (id == R.id.nav_signin) {
            Intent in = new Intent( getApplicationContext(), LoginActivity.class );
            startActivity( in );
            finish();
        }

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void showSortDialog() {
        String[] sortOptions = {"Destacados", "Nuevos", "Antiguos"};
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Ver por" )
                .setItems( sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ProfileFragment profileFragment = new ProfileFragment();

                            Bundle args = new Bundle();
                            args.putString( "sort", "bests" );
                            profileFragment.setArguments( args );

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add( R.id.container, profileFragment );
                            fragmentTransaction.commit();

                        } else if (which == 1) {
                            ProfileFragment profileFragment = new ProfileFragment();

                            Bundle args = new Bundle();
                            args.putString( "sort", "new" );
                            profileFragment.setArguments( args );

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add( R.id.container, profileFragment );
                            fragmentTransaction.commit();
                        } else if (which == 2) {
                            ProfileFragment profileFragment = new ProfileFragment();

                            Bundle args = new Bundle();
                            args.putString( "sort", "old" );
                            profileFragment.setArguments( args );

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add( R.id.container, profileFragment );
                            fragmentTransaction.commit();
                        }
                    }
                } );
        builder.show();
    }

    @SuppressLint("RestrictedApi")
    public void updateNavHeader() {

        navigationView = findViewById( R.id.nav_view );
        final View headerView = navigationView.getHeaderView( 0 );
        TextView navUsername = headerView.findViewById( R.id.nav_username );
        TextView navUserMail = headerView.findViewById( R.id.nav_user_email );
        final TextView navUserPlan = headerView.findViewById( R.id.nav_user_plan );
        final ImageView navUserPhot = headerView.findViewById( R.id.nav_user_photo );


        String userid = currentUser.getUid();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                .child( "usuarios" )
                .child( userid )
                .child( "plan" );
        mReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String mPlan = (String) (dataSnapshot.getValue());

                navUserPlan.setText( mPlan );

                if (mPlan.equals( "Con plan de paga" )) {
                    FloatingActionButton fab = findViewById( R.id.fab );
                    fab.setVisibility( View.VISIBLE );
                } else {
                    FloatingActionButton fab = findViewById( R.id.fab );
                    fab.setVisibility( View.VISIBLE );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Error linea 678: " + databaseError.getMessage() );
            }
        } );

        navUserMail.setText( currentUser.getEmail() );
        navUsername.setText( currentUser.getDisplayName() );

        // Usamos la libreria de glide para cargar la imagen del usuario

        if (currentUser.getPhotoUrl() != null) {
            Glide.with( this ).load( currentUser.getPhotoUrl() ).into( navUserPhot );

        } else {
            Glide.with( this ).load( R.drawable.userphoto ).into( navUserPhot );
        }


    }

}
