package com.example.tonyayala.empectory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tonyayala.empectory.Adapters.CommentAdapter;
import com.example.tonyayala.empectory.Models.Comment;
import com.example.tonyayala.empectory.Models.Favorite;
import com.example.tonyayala.empectory.Models.Like;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static Notification_Channel.App.CHANNEL_ID;

public class PostDetailActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;


    ImageView imgPost;
    ImageView imgUserPost;
    ImageView imgCurrentUser;
    ImageView imgFacebook;
    ImageView imgLike;
    ImageView back;
    ImageView imgDltPost;
    TextView txtPostDesc;
    TextView txtPostDateName;
    TextView txtPostTittle;
    TextView txtPostPhone;
    TextView txtPostEmail;
    TextView txtPostAddress;
    TextView txtCountLikes;
    EditText editTextComment;
    String postKey;
    String userId;
    Button btnAddComment;
    FloatingActionButton fabMain;
    FloatingActionButton fabEdit;
    FloatingActionButton fabMarket;
    FloatingActionButton fabMarketWithoutProperty;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    Dialog myDialog;

    ProgressDialog progressDialog;

    int count;
    int postCount;
    String mHistory = "";

    static String COMMENT_KEY = "Comment";

    private static final SecureRandom rgenerator = new SecureRandom();

    Float translationY = 100f;
    Boolean isMenuOpen = false;
    OvershootInterpolator interpolator = new OvershootInterpolator(  );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_post_content);

        myDialog = new Dialog( this );

        progressDialog = new ProgressDialog( this );

        //Añadiremos una barra de estado transparente
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getSupportActionBar().hide();

        Toolbar toolbar = findViewById( R.id.toolbar_post );
        toolbar.bringToFront();

        back = findViewById( R.id.btn_post_detail_back );
        back.bringToFront();

        //Inicializar Views

        RvComment = findViewById(R.id.rv_comment);
        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);
        imgFacebook = findViewById(R.id.post_detail_facebook_img);
        imgLike = findViewById(R.id.post_detail_liked);
        imgDltPost = findViewById( R.id.post_detail_delete );

        txtPostTittle = findViewById(R.id.post_detail_tittle);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        txtPostPhone = findViewById(R.id.post_detail_phone);
        txtPostEmail = findViewById(R.id.post_detail_email);
        txtPostAddress = findViewById(R.id.post_detail_adrress);
        txtCountLikes = findViewById(R.id.post_detail_count);

        fabMarketWithoutProperty = findViewById( R.id.fab_market_without_propierty );

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        notificationManager = NotificationManagerCompat.from( this );


        imgUserPost.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        } );

        //Añadir boton comentar OnClick
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(postKey).push();
                String commentKey = commentReference.getKey();
                String commenContent = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl().toString();
                //Se envian todos lo parametros a la BD
                Comment comment = new Comment(commenContent ,uid,uimg,uname,commentKey);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Se añadió comentarío");
                        editTextComment.setText("");
                        btnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Ocurrió un erro al comentar : "+e.getMessage());
                    }
                });

            }
        });


        back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( PostDetailActivity.this, PrincipalActivity.class ) );
                finish();
            }
        } );

        fabMarketWithoutProperty.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( PostDetailActivity.this, Market.class );
                String postkey = postKey;
                //ID del usuario del post
                intent.putExtra( "userId", userId );
                intent.putExtra("postkey", postkey);
                startActivity( intent );
            }
        } );

        imgDltPost.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questDeletePost();
            }
        } );

        initFabMenu();

    }

    private void notification() {
        //Titulo de la empresa
        final String postTittle = getIntent().getExtras().getString("tittle");

        //Objeto para notificacion, el CHANNEL_ID es una clase
        Notification notification = new NotificationCompat.Builder( this, CHANNEL_ID )
                .setSmallIcon( R.mipmap.ic_launcher )
                .setContentTitle( "Han comentado tu empresa" )
                .setContentText( "Un usuario ha dado una reseña para: " + postTittle)
                .setPriority( NotificationCompat.PRIORITY_HIGH )
                .setCategory( NotificationCompat.CATEGORY_MESSAGE )
                .build();

        notificationManager.notify( 1, notification );
    }

    private void initFabMenu() {
        fabMain = findViewById(R.id.fab_main);
        fabEdit = findViewById(R.id.fab_edit);
        fabMarket = findViewById(R.id.fab_market);

        fabEdit.setAlpha( 0f );
        fabMarket.setAlpha( 0f );

        fabEdit.setTranslationY( translationY );
        fabMarket.setTranslationY( translationY );

        fabMain.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMenuOpen){
                    closeMenu();
                }else{
                    openMenu();
                }
            }
        } );


    }

    private void openMenu(){
        //Metodo para abrir el menu flotante
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator( interpolator ).rotation( 45f ).setDuration( 300 ).start();

        fabEdit.animate().translationY( 0f ).alpha( 1f ).setInterpolator( interpolator ).setDuration( 300 ).start();
        fabMarket.animate().translationY( 0f ).alpha( 1f ).setInterpolator( interpolator ).setDuration( 300 ).start();
    }

    private void closeMenu(){
        //Metodo para cerrar el menu flotante
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator( interpolator ).rotation( 0f ).setDuration( 300 ).start();

        fabEdit.animate().translationY( translationY ).alpha( 0f ).setInterpolator( interpolator ).setDuration( 300 ).start();
        fabMarket.animate().translationY( translationY ).alpha( 0f ).setInterpolator( interpolator ).setDuration( 300 ).start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        try {
            if (user != null) {
                if(user.isEmailVerified()) {
                    setData();
                    getData();
                    CountLike();
                    setLike();

                }else{
                    setDatosWithdOutSign();
                    getData();
                }
            } else {
                setDatosWithdOutSign();
                getData();
            }
        } catch (Exception x) {
            showMessage("error 160: "+ x.getMessage());
        }
    }
        //Funcion ver empresas sin credenciales de inicio de sesion
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "RestrictedApi"})
    private void setDatosWithdOutSign(){
        //Boton comentar habilitado es necesario iniciar sesion para poder comentar
        editTextComment.setEnabled(false);
        editTextComment.setHint("Inicie sesión para comentar");
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDetailActivity.this, LoginActivity.class));
                editTextComment.setText("");
            }
        });

        //Metodo para colocar una foto aleatoria en el campo para comentar al no haber iniciado sesion
        Integer[] aleatory = {R.drawable.userphoto, R.drawable.userphotow, R.drawable.userphotot};
        Integer random = aleatory[rgenerator.nextInt(aleatory.length)];

            //Carga una imagen aleatoria en la seccion de comentarios
            Glide.with(this).load(random).into(imgCurrentUser);

            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessage("Inicie sesión para agregar a favoritos");
                }
            });

        fabMarketWithoutProperty.setVisibility( View.VISIBLE );

    }


    @SuppressLint("RestrictedApi")
    private void getData(){

        imgFacebook = findViewById(R.id.post_detail_facebook_img);

        //Recibimos los datos y los asignamos en los respectivos campos

        final String postImage = getIntent().getExtras().getString("postImage");
        Glide.with( this ).load( postImage ).into( imgPost );


        final String postTittle = getIntent().getExtras().getString("tittle");
        txtPostTittle.setText(postTittle);

        String userPostImage = getIntent().getExtras().getString("userPhoto");

        if(userPostImage != null){
            Glide.with(this).load(userPostImage).into(imgUserPost);
        }else {
            Integer[] aleatory = {R.drawable.userphoto, R.drawable.userphotow, R.drawable.userphotot};
            Integer random = aleatory[rgenerator.nextInt(aleatory.length)];
            Glide.with( this ).load( random ).into( imgUserPost );
        }


        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        String postPhone = getIntent().getExtras().getString("phone");
        txtPostPhone.setText(postPhone);

        String postEmail = getIntent().getExtras().getString("email");
        txtPostEmail.setText(postEmail);

        final String postAddress = getIntent().getExtras().getString("address");
        txtPostAddress.setText(postAddress);

        final String postFacebook = getIntent().getExtras().getString("facebook");
        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookIntent(postFacebook);
            }
        });

        //ID del post
        postKey = getIntent().getExtras().getString("postKey");


        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);

        //Se inicia el RecyclerView


        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, EditPostActivity.class);
                String image = postImage;
                String tittle = txtPostTittle.getText().toString();
                String description = txtPostDesc.getText().toString();
                String phone = txtPostPhone.getText().toString();
                String address = txtPostAddress.getText().toString();
                String postkey = postKey;
                intent.putExtra("image", image);
                intent.putExtra("tittle",tittle);
                intent.putExtra("description", description);
                intent.putExtra("phone", phone);
                intent.putExtra("address",address);
                intent.putExtra("postkey", postkey);
                startActivity(intent);
            }
        });

        fabMarket.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( PostDetailActivity.this, Market.class );
                String postkey = postKey;
                intent.putExtra( "userId", userId );
                intent.putExtra("postkey", postkey);
                startActivity(intent);

            }
        } );


    }

    //Funcion ver empresa con credenciales
    @SuppressLint("RestrictedApi")
    private void setData(){
        //Añadir foto al usuario por comentar el post
        if(firebaseUser.getPhotoUrl() != null){
            //Si tiene foto de perfil
            Glide.with(this).load( firebaseUser.getPhotoUrl()).into( imgCurrentUser );
        }else{
            //Si no tiene foto de perfil
            Glide.with(this).load(R.drawable.userphoto).into(imgCurrentUser);
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser = firebaseUser.getUid();
        //ID del usuario del post
        userId = getIntent().getExtras().getString("userId");

        //Verificar los ID del post y del ususario actual para veridficar si es o no propietario de la empresa

        if(currentUser.equals(userId)){
            iniRvComment();
          //  showMessage( "usuarios iguales" );
            fabMain.setVisibility( View.VISIBLE );
            fabEdit.setVisibility( View.VISIBLE );
            fabMarket.setVisibility( View.VISIBLE );
            imgDltPost.setVisibility( View.VISIBLE );
        }else{
          // showMessage( "no prro");
            fabMarketWithoutProperty.setVisibility( View.VISIBLE );
            iniRvCommentWithOutUserPropiety();
        }

    }

    private void questDeletePost() {

        dialogDelete();

    }

    private void dialogDelete() {
      final AlertDialog.Builder builder =  new AlertDialog.Builder( this );
      builder.setTitle( "¡Alerta!" );
      LinearLayout linearLayout = new LinearLayout( this );
      final TextView txtMsg = new TextView(this);
      txtMsg.setText( "Está a punto de eliminar su publicación. ¿Está seguro que desea continuar?" );
      txtMsg.setMinEms( 16 );
      linearLayout.addView( txtMsg );
      linearLayout.setPadding( 15,15,15,15 );
      builder.setView( linearLayout );
      builder.setPositiveButton( "Continuar", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            progressDialog.setMessage( "Eliminando" );
            progressDialog.show();
            deletePost();
          }
      } );
      builder.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
      } );

      builder.create().show();
    }

    private void deletePost() {
        DatabaseReference databaseReference = firebaseDatabase.getReference("Posts").child( postKey );
        databaseReference.removeValue();
        progressDialog.dismiss();
        Intent intent = new Intent( PostDetailActivity.this, PrincipalActivity.class );
        startActivity( intent );
    }

    private void facebookIntent(String postFacebook){
        //Ejecuta el enlace hacia facebook
        Intent intentF = new Intent();
        intentF.setAction(Intent.ACTION_VIEW);
        intentF.addCategory(Intent.CATEGORY_BROWSABLE);
        intentF.setData(Uri.parse(postFacebook));

        startActivity(intentF);
    }

    private void iniRvComment() {
        //Metodo con notificacion
        postKey = getIntent().getExtras().getString("postKey");

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ArrayList que contendra los datos de la empresa
                listComment = new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    //Recorrido en la BD para obtener los datos de la empresa
                    Comment comment= snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);

                //Si se detecta cambio en la BD, que se ejecute la notificacion
                if(dataSnapshot.exists()){
                    notification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Error con la base de datos" + databaseError );
            }
        });
    }

    private void iniRvCommentWithOutUserPropiety() {
        //Metodo que no tiene notificacion
        postKey = getIntent().getExtras().getString("postKey");
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ArrayList que contendra todos los datos de la empresa (Consultar arriba)
                listComment = new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Comment comment= snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Error con la base de datos" + databaseError );
            }
        });
    }


    private void showHistory() {
        //Referencia al nodo Historia de la BD
        DatabaseReference reference = firebaseDatabase.getReference( "Historia" ).child( postKey );
                reference.child( "history" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String history = dataSnapshot.getValue(String.class);
                    mHistory = history;
                    dialogHistory();
                }else{
                    showMessage( "No hay historia por parte del emprendedor" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showMessage( "Error 373: " + databaseError );
            }
        } );
    }

    private void dialogHistory() {
        //Ejecuta un Custom AlertDialog
        TextView txtClose;
        TextView txtHistory;

        myDialog.setContentView( R.layout.activity_showhistory );
        txtClose = myDialog.findViewById( R.id.close_history_TV );
        txtHistory = myDialog.findViewById( R.id.history_TV );

        txtHistory.setText( mHistory );

        txtClose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        } );

        myDialog.show();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time){
        //Establecemos el formato del calendario
        Calendar calendar = Calendar.getInstance(Locale.FRENCH);
        calendar.setTimeInMillis(time);
        //Establecemos el formato de la fecha que guardaremos
        String date = android.text.format.DateFormat.format("dd/MMM/yyyy",calendar).toString();
        return date;

    }

    private void setLike(){
    //Colores
    final int redColor = getResources().getColor(R.color.RED);
    final int blackColor = getResources().getColor(R.color.black);


    //Contadores

    DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Posts");
    // Contador de likes
    mReference.child(postKey)
            .child("like").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                //Se obtiene la cantidad entera de la cantidad de Likes que hay en la empresa
                final int mCount = dataSnapshot.getValue( Integer.class );
                postCount = mCount;

                txtCountLikes.setText( String.valueOf( postCount ) );

                //ID del post
                postKey = getIntent().getExtras().getString( "postKey" );

                final String user = firebaseAuth.getUid();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();


                //Contador del usuario
                final DatabaseReference userRef = database.getReference( "usuarios" ).child( user ).child( "Likes" ).child( postKey );
                final DatabaseReference favRef = database.getReference( "usuarios" ).child( user ).child( "Favorites" ).child( postKey );

                userRef.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            //Se obtiene un contador en el usuario para determinar si ya ha votado
                            String userco = dataSnapshot.getKey();

                            if (userco.equals( postKey )) {
                                int countUser = dataSnapshot.child( "count" ).getValue( Integer.class );
                                //Se obtiene el valor del contador
                                count = countUser;
                                if (countUser == 1) {

                                    count--;
                                    postCount--;
                                    imgLike.setColorFilter( redColor );

                                } else if (countUser == 0) {
                                    count++;
                                    postCount++;
                                    imgLike.setColorFilter( blackColor );
                                    favRef.removeValue();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage( "Error 370: " + databaseError );
                    }
                } );
                //Termina el contador

            } else {
                //
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            showMessage("Error 561: "+ databaseError);
        }
    });
    //Termina el contador


}

    private void CountLike(){
        try {

            //Mustra total de likes
            //ID del post
            final String postImage = getIntent().getExtras().getString("postImage");
            final String postTittle = getIntent().getExtras().getString("tittle");
            postKey = getIntent().getExtras().getString("postKey");

            final String user = firebaseAuth.getUid();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference userRef = database.getReference("usuarios");
            final DatabaseReference likeRef = database.getReference("Posts");

            imgLike.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    final Like like = new Like(count);
                    userRef.child(user).child( "Likes" ).child(postKey).setValue(like);


                    // Aumentara uno en la base de datos del lado del post, cuando el usuario cambie en el lado de usuario
                    likeRef.child(postKey).child("like").setValue(postCount);

                    Favorite favorite = new Favorite( postTittle, postImage, userId, postKey );
                    userRef.child( user ).child( "Favorites" ).child( postKey ).setValue( favorite );

                }
            });


        }catch (Exception x){
            showMessage("Error 357: " + x.getMessage());
        }
    }
}
