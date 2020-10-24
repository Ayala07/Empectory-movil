package com.example.tonyayala.empectory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyayala.empectory.Adapters.HelpAdapter;

import java.util.ArrayList;

public class help2Activity extends AppCompatActivity {
    ListView listView;
    ArrayList<Integer> listIds;
    ArrayList<String> nameList;
    ArrayList<String> subnameList;
    HelpAdapter helpAdapter;
    Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_help2 );
        //Definimos el ListView que esta en el activity de help
        myDialog = new Dialog( this );


        listView = findViewById( R.id.help_list );

        //Creamos tres ArrayList, que serán los que contengan la informacion y serán mostrados en en ListView
        listIds = new ArrayList<>();
        nameList = new ArrayList<>();
        subnameList = new ArrayList<>();

        //Obtenemos los datos que se han definido en los ArrayList
        listIds = getListIds();
        nameList = getNameList();
        subnameList = getSubnameList();
        //Le asignamos al adaptador los datos que se han obtenido de los ArrayList
        helpAdapter = new HelpAdapter( help2Activity.this, listIds, nameList, subnameList );
        listView.setAdapter( helpAdapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Condicional para detectar la accion que se este realizandp
                switch (nameList.get( position )) {
                    case "Sobre Nosotros":
                        startActivity( new Intent( help2Activity.this, historyActivity.class ) );

                        break;
                    case "Preguntas frecuentes":
                        startActivity( new Intent( help2Activity.this, faq2Activity.class ) );

                        break;
                    case "Redes Sociales de Empectory":
                        showAbout();
                        break;

                    case "Chat Bot":
                        startActivity( new Intent( help2Activity.this, ChatBotActivity.class ) );
                        break;

                    case "Reportar un error":
                        startActivity( new Intent( help2Activity.this, Feddback.class ) );
                        break;

                    case "Términos y condiciones":
                        termsAndConditions();
                        break;

                    case "Info. de la aplicación":
                        startActivity( new Intent( help2Activity.this, info_application.class ) );
                        break;

                        default:
                            Toast.makeText( help2Activity.this, "error inesperado", Toast.LENGTH_SHORT ).show();
                            break;
                }
            }
        } );

    }

    public void showAbout() {
        TextView txtclose;
        LinearLayout facebook;
        LinearLayout gmail;

        myDialog.setContentView( R.layout.activity_about );

        txtclose = myDialog.findViewById( R.id.close_about_TV );

        facebook = myDialog.findViewById( R.id.facebookL );

        gmail = myDialog.findViewById( R.id.gmailL );

        txtclose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        } );

        facebook.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookIntent();
            }
        } );

        gmail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        } );

        myDialog.show();


    }

    private void sendEmail() {
        // Reemplazamos el email por algun otro real
        String[] to = {"empectory@gmail.com"};
        String[] cc = {""};
        enviar( to, cc, "",
                "" );
    }

    @SuppressLint("IntentReset")
    private void enviar(String[] to, String[] cc,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent( Intent.ACTION_SEND );
        emailIntent.setData( Uri.parse( "mailto:" ) );
        emailIntent.putExtra( Intent.EXTRA_EMAIL, to );
        emailIntent.putExtra( Intent.EXTRA_CC, cc );
        emailIntent.putExtra( Intent.EXTRA_SUBJECT, asunto );
        emailIntent.putExtra( Intent.EXTRA_TEXT, mensaje );
        emailIntent.setType( "message/rfc822" );
        startActivity( Intent.createChooser( emailIntent, "Email " ) );
    }

    private void termsAndConditions() {
        Intent intentT = new Intent();
        intentT.setAction( Intent.ACTION_VIEW );
        intentT.addCategory( Intent.CATEGORY_BROWSABLE );
        intentT.setData( Uri.parse( "https://empectory3.000webhostapp.com/public/vistas/terminos_politicas_privacidad.html" ) );
        startActivity( intentT );
    }

    public void facebookIntent() {
        Intent intentF = new Intent();
        intentF.setAction( Intent.ACTION_VIEW );
        intentF.addCategory( Intent.CATEGORY_BROWSABLE );
        intentF.setData( Uri.parse( "https://m.facebook.com/Empectory-855169551550158/" ) );
        startActivity( intentF );
    }

    //Definimos datos en el ArrayList
    public ArrayList<Integer> getListIds() {
        listIds = new ArrayList<>();
        listIds.add( R.drawable.ic_help_outline_black_24dp );
        listIds.add( R.drawable.ic_people_black_24dp );
        listIds.add( R.drawable.social_network );
        listIds.add( R.drawable.robot );
        listIds.add( R.drawable.reportpng );
        listIds.add( R.drawable.terminos );
        listIds.add( R.drawable.ic_info_outline_black_24dp );
        return listIds;
    }

    //Definimos datos en el ArrayList
    public ArrayList<String> getNameList() {
        nameList = new ArrayList<>();
        nameList.add( "Preguntas frecuentes" );
        nameList.add( "Sobre Nosotros" );
        nameList.add( "Redes Sociales de Empectory" );
        nameList.add( "Chat Bot" );
        nameList.add( "Reportar un error" );
        nameList.add( "Términos y condiciones" );
        nameList.add( "Info. de la aplicación" );
        return nameList;
    }

    //Definimos datos en el ArrayList
    public ArrayList<String> getSubnameList() {
        subnameList = new ArrayList<>();
        subnameList.add( "¿Preguntas?" );
        subnameList.add( "¿Quieres saber sobre Empectory?" );
        subnameList.add( "" );
        subnameList.add( "" );
        subnameList.add( "" );
        subnameList.add( "" );
        subnameList.add( "" );
        return subnameList;
    }


}
