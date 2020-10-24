package com.example.tonyayala.empectory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatBotActivity extends AppCompatActivity {

    TextView messagesTextView;
    EditText inputEditText;
    Button sendButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        context = this;
        messagesTextView =  findViewById(R.id.messagesTextView);
        inputEditText =  findViewById(R.id.inputEditText);
        sendButton =  findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos el texto de la caja de texto y lo instanciamos en el messagesTextView
                String input = inputEditText.getText().toString();
                messagesTextView.append(Html.fromHtml("<p><b>Tu:</b>  " + input + "</p>"));
                inputEditText.setText("");

                getResponse(input);
            }
        });


    }

    private void getResponse(String input) {

        String worksSpaceId = "935b4d07-e86e-4b11-bdcb-19156ff6aee6";
        String urlAssintant = "https://gateway.watsonplatform.net/assistant/api/v1/workspaces/"+worksSpaceId+"/message?version=2019-02-28";
        String authentication = "YXBpa2V5OkJUdWJ5TUtEcjk2OGhoUjE3eUpkTHpvTDItUktTTkJiQ2Q0TTB2NUhhZWRE";

        //Crear estructura JSON de input del ususario

        JSONObject inputJsonObject = new JSONObject();
        try {
            inputJsonObject.put("text", input);
        } catch (JSONException e) {
            showMessage( "Error: " + e );
        }


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("input", inputJsonObject);
        } catch (JSONException e) {
            showMessage( "Error: " + e );
        }

        AndroidNetworking.post(urlAssintant)
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Basic "+authentication)
                .addJSONObjectBody(jsonBody)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Parsear la respuesta de JSON
                        try {
                            String outputJsonObject = response.getJSONObject("output").getJSONArray("text").getString(0);
                            messagesTextView.append(Html.fromHtml("<p><b>Asistente:</b>  " + outputJsonObject + "</p>"));
                        } catch (JSONException e) {
                            showMessage("error" + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        showMessage( "error de conexion" );
                    }
                });

    }

    private void showMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
    }
}
